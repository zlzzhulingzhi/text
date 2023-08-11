package cn.qbs.wa.union.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.qbs.wa.teach.common.core.config.RSAConfig;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.enums.UniClientCodeEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.exception.auth.NotPermissionException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeeDTO;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsEmployeeService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.LoginInfoDTO;
import cn.qbs.wa.union.auth.entity.*;
import cn.qbs.wa.union.auth.enumerate.ForbiddenTypeEnum;
import cn.qbs.wa.union.auth.mapper.UniUserMiniappMapper;
import cn.qbs.wa.union.auth.pojo.login.*;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserDTO;
import cn.qbs.wa.union.auth.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author vieux
 * @version 1.0
 * @date 2022/7/8 15:46
 */
@Slf4j
@RefreshScope
@Service
public class SystemLoginServiceImpl implements SystemLoginService {

    @Autowired
    UniUserService uniUserService;

    @Autowired
    SystemUserService systemUserService;

    @Autowired
    SystemEmployeeUserService systemEmployeeUserService;

    @Autowired
    UniUserOauthService systemUserOauthService;

    @Autowired
    SystemApplicationService systemApplicationService;

    @Autowired
    UniUserMiniappMapper uniUserMiniappMapper;

    @Autowired
    RemoteTrainLogisticsEmployeeService logisticsEmployeeService;

    @Autowired
    RemoteEmployeeService teachEmployeeService;

    @Autowired
    RedisService redisService;

    /**
     * 禁用时长
     */
    @Value("${global.forbiddenDuration:60}")
    private Long forbiddenDuration;

    private long checkForbidden(String key, String type) {
        Object forbidden = redisService.getCacheObject(CacheConstants.FORIBDDEN_CODE + key);
        if (forbidden != null) {
            throw new ServiceException(String.format("该账号已停用,请%s分钟后重新尝试", forbidden));
        }
        long forbiddenLimit = 0;
        Object forbiddenCount = redisService.getCacheObject(CacheConstants.FORIBDDEN_CODE + type  + ":" + key);
        if (forbiddenCount != null) {
            forbiddenLimit = Long.parseLong(forbiddenCount.toString());
            if (forbiddenLimit >= CacheConstants.FORIBDDEN_COUNT) {
                if (ForbiddenTypeEnum.LOGIN.getType().equals(type)) {
                    redisService.setCacheObject(CacheConstants.FORIBDDEN_CODE + key, this.forbiddenDuration, this.forbiddenDuration, TimeUnit.MINUTES);
                    throw new ServiceException(String.format("该账号已停用,请%d分钟后重新尝试", this.forbiddenDuration));
                } else if (ForbiddenTypeEnum.PASSWORD.getType().equals(type)) {
                    redisService.setCacheObject(CacheConstants.FORIBDDEN_CODE + key, this.forbiddenDuration, this.forbiddenDuration, TimeUnit.MINUTES);
                    // 移除验证码
                    redisService.getCacheObject(CacheConstants.FORGET_PWD_CODE + key);
                    // 注销当前登录
                    AuthUtil.logout();
                    throw new ServiceException(String.format("该账号已停用,请%d分钟后重新尝试", this.forbiddenDuration));
                }
            }
        }
        return forbiddenLimit;
    }

    private void incrForbidden(String key, String type, long duration) {
        long forbiddenLimit = 1;
        Object forbiddenCount = redisService.getCacheObject(CacheConstants.FORIBDDEN_CODE + type  + ":" + key);
        if (forbiddenCount != null) {
            forbiddenLimit = Long.parseLong(forbiddenCount.toString()) + 1;
        }
        redisService.setCacheObject(CacheConstants.FORIBDDEN_CODE + type  + ":" + key, forbiddenLimit, duration, TimeUnit.MINUTES);
    }

    private void clearForbidden(String key, String type) {
        redisService.deleteObject(CacheConstants.FORIBDDEN_CODE + type  + ":" + key);
        redisService.deleteObject(CacheConstants.FORIBDDEN_CODE + key);
    }

    @Override
    public LoginUser login(LoginRequest request) {

        String account = request.getAccount();

        String forbiddenType = ForbiddenTypeEnum.LOGIN.getType();
        long forbiddenLimit = this.checkForbidden(account, forbiddenType);

        UniUserDTO uniUserDTO = getExistUser(account, request.getLoginCode());

        if (uniUserDTO == null) {
            throw new ServiceException("登录用户：" + account + " 不存在");
        }

        String password = decodePassWord(request.getPassword());
        if (!SecurityUtils.matchesPassword(password, uniUserDTO.getPassword())) {
            // 账号密码错误，记录尝试次数
            incrForbidden(account, forbiddenType, this.forbiddenDuration);
            throw new ServiceException("用户不存在/密码错误");
        }
        if (forbiddenLimit > 0) {
            // 登录成功 清除密码错误次数记录
            clearForbidden(account, forbiddenType);
        }
        LoginUser loginUser = BeanUtil.copyProperties(uniUserDTO, LoginUser.class);
        loginUser.setSysUser(BeanUtil.copyProperties(uniUserDTO, SysUser.class, "password"));
        loginUser.setUserType(UniClientCodeEnum.ADMIN.getCode().equals(request.getLoginCode()) ? UserConstants.USER_PLATFORM : UserConstants.USER_EMPLOYEE);
        return loginUser;
    }

    @Override
    public LoginUser loginBySms(LoginRequest request) {
        UniUserDTO uniUserDTO = getExistUser(request.getAccount(), request.getLoginCode());

        if (uniUserDTO == null) {
            throw new ServiceException("登录用户：" + request.getAccount() + " 不存在");
        }
        LoginUser loginUser = BeanUtil.copyProperties(uniUserDTO, LoginUser.class);
        loginUser.setSysUser(BeanUtil.copyProperties(uniUserDTO, SysUser.class));
        loginUser.setUserType(UniClientCodeEnum.ADMIN.getCode().equals(request.getLoginCode()) ? UserConstants.USER_PLATFORM : UserConstants.USER_EMPLOYEE);
        return loginUser;
    }

    @Override
    public void checkUser(String account, String action, String appCode) {
        UniUserDTO existUser = getExistUser(account, appCode);
        if (existUser == null) {
            throw new ServiceException("登录用户：" + account + " 不存在");
        }
    }

    @Override
    public void resetPassword(UserPasswordRequest request) {
        // 获取当前登录人的手机号码
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || StrUtil.isBlank(loginUser.getAccount())) {
            throw new NotPermissionException("没有访问权限");
        }
        String phone = loginUser.getAccount();
        String forbiddenType = ForbiddenTypeEnum.PASSWORD.getType();
        long forbiddenLimit = this.checkForbidden(phone, forbiddenType);
        Object cacheCode = redisService.getCacheObject(CacheConstants.FORGET_PWD_CODE + phone);
        if (cacheCode == null) {
            // 验证码错误，记录尝试次数
            incrForbidden(phone, forbiddenType, this.forbiddenDuration);
            throw new ServiceException("验证码已过期请重新再试");
        }
        String code = cacheCode.toString();
        if (!code.equals(request.getCode())) {
            // 验证码错误，记录尝试次数
            incrForbidden(phone, forbiddenType, this.forbiddenDuration);
            throw new ServiceException("验证码错误,请重新输入");
        }
        updatePasswordExistUser(request.getLoginCode(), request.getPassword());
        // 移除短信验证码
        redisService.deleteObject(CacheConstants.FORGET_PWD_CODE + phone);
        if (forbiddenLimit > 0) {
            // 成功 清除错误次数记录
            clearForbidden(phone, forbiddenType);
        }
    }

    @Override
    public LoginUser loginByWechat(SocialLoginRequest request) {
        SystemApplication systemApplication = systemApplicationService.getOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, request.getLoginCode()));
        if (systemApplication != null) {
            List<UniUserOauth> userOauthList = systemUserOauthService.list(new LambdaQueryWrapper<UniUserOauth>()
                    .eq(UniUserOauth::getAppId, systemApplication.getId()).eq(UniUserOauth::getUid, request.getUnionId()));
            if (CollUtil.isNotEmpty(userOauthList)) {
                UniUserOauth uniUserOauth = userOauthList.get(0);
                UniUserDTO uniUserDTO = getExistUser(uniUserOauth.getUserId(), request.getLoginCode());
                if (uniUserDTO == null) {
                    throw new ServiceException("登录用户,不存在");
                }
                LoginUser loginUser = BeanUtil.copyProperties(uniUserDTO, LoginUser.class);
                loginUser.setSysUser(BeanUtil.copyProperties(uniUserDTO, SysUser.class));
                return loginUser;
            }
        }
        return null;
    }

    /**
     * 根据手机号更改密码
     * @param appCode
     * @param password
     */
    private void updatePasswordExistUser(String account, String appCode, String password) {
        account = AesUtil.encode(account);
        UniClientCodeEnum uniClientCodeEnum = UniClientCodeEnum.getEnumByCode(appCode);
        switch (uniClientCodeEnum) {
            case ADMIN:
                updateAdminPassword(account, password);
                break;
            case ORG:
                updateEmployeePassword(account, password);
                break;
        }
    }

    /**
     * 根据当前登录人更改密码
     * @param appCode
     * @param password
     */
    private void updatePasswordExistUser(String appCode, String password) {
        UniClientCodeEnum uniClientCodeEnum = UniClientCodeEnum.getEnumByCode(appCode);
        switch (uniClientCodeEnum) {
            case ADMIN:
                updateAdminPassword(password);
                break;
            case ORG:
                updateEmployeePassword(password);
                break;
        }
    }

    private void updateEmployeePassword(String password) {
        Long userId = SecurityContextHolder.getUserId();
        SystemEmployeeUser systemEmployeeUser = systemEmployeeUserService.getById(userId);
        if (systemEmployeeUser != null) {
            if (Constants.DB_FAIL.equals(systemEmployeeUser.getEnabled())) {
                throw new ServiceException("账号已禁用");
            }
            SystemUser updater = new SystemUser();
            updater.setId(userId);
            systemEmployeeUser.setPassword(SecurityUtils.encryptPassword(decodePassWord(password)));
            systemEmployeeUserService.updateById(systemEmployeeUser);
        }
    }

    private void updateAdminPassword(String password) {
        Long userId = SecurityContextHolder.getUserId();
        SystemUser systemUser = systemUserService.getById(userId);
        if (systemUser != null) {
            if (Constants.DB_FAIL.equals(systemUser.getEnabled())) {
                throw new ServiceException("账号已禁用");
            }
            SystemUser updater = new SystemUser();
            updater.setId(systemUser.getId());
            systemUser.setPassword(SecurityUtils.encryptPassword(decodePassWord(password)));
            systemUserService.updateById(updater);
        }
    }

    private void updateEmployeePassword(String account, String password) {
        SystemEmployeeUser systemEmployeeUser = systemEmployeeUserService.getOne(new LambdaQueryWrapper<SystemEmployeeUser>().select(SystemEmployeeUser::getId).eq(SystemEmployeeUser::getAccount, account));
        if (systemEmployeeUser != null) {
            systemEmployeeUser.setPassword(SecurityUtils.encryptPassword(password));
            systemEmployeeUserService.updateById(systemEmployeeUser);
        }
    }

    private void updateAdminPassword(String account, String password) {
        SystemUser systemUser = systemUserService.getOne(new LambdaQueryWrapper<SystemUser>().select(SystemUser::getId).eq(SystemUser::getAccount, account));
        if (systemUser != null) {
            systemUser.setPassword(SecurityUtils.encryptPassword(password));
            systemUserService.updateById(systemUser);
        }
    }

    private UniUserDTO getExistUser(Long userId, String appCode) {
        //这里account需要加密
        UniClientCodeEnum uniClientCodeEnum = UniClientCodeEnum.getEnumByCode(appCode);
        UniUserDTO uniUserDTO = null;
        switch (uniClientCodeEnum) {
            case ADMIN:
                uniUserDTO = adminLogin(userId);
                break;
            case ORG:
                uniUserDTO = employeeLogin(userId);
                break;
        }
        return uniUserDTO;
    }

    private UniUserDTO employeeLogin(Long userId) {
        return BeanUtil.copyProperties(systemEmployeeUserService.getOne(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getId, userId)), UniUserDTO.class);

    }

    private UniUserDTO adminLogin(Long userId) {
        return BeanUtil.copyProperties(systemUserService.getOne(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getId, userId)), UniUserDTO.class);

    }

    @Override
    public UniUserDTO getExistUser(String account, String appCode) {
        //这里account需要加密
        account = AesUtil.encode(account);
        UniClientCodeEnum uniClientCodeEnum = UniClientCodeEnum.getEnumByCode(appCode);
        UniUserDTO uniUserDTO = null;
        switch (uniClientCodeEnum) {
            case ADMIN:
                uniUserDTO = adminLogin(account);
                break;
            case ORG:
                uniUserDTO = employeeLogin(account);
                break;
        }
        if (uniUserDTO != null) {
            // 手机号解密
            uniUserDTO.setAccount(AesUtil.decode(uniUserDTO.getAccount()));
            uniUserDTO.setPhone(AesUtil.decode(uniUserDTO.getPhone()));
        }
        return uniUserDTO;
    }

    @Override
    public Boolean checkEmployeePhoneBindingInfo(String account) {
        SystemEmployeeUser systemEmployeeUser = systemEmployeeUserService.getOne(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getAccount, AesUtil.encode(account)));
        if (systemEmployeeUser == null) {
            return true;
        }
        return CollUtil.isNotEmpty(systemUserOauthService.list(new LambdaQueryWrapper<UniUserOauth>().eq(UniUserOauth::getUserId,systemEmployeeUser.getId())));
    }

    private UniUserDTO employeeLogin(String account) {
        SystemEmployeeUser employeeUser = systemEmployeeUserService.getOne(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getAccount, account));
        if (employeeUser != null) {
            return BeanUtil.copyProperties(employeeUser, UniUserDTO.class);
        }
        return null;
    }

    private UniUserDTO adminLogin(String account) {
        SystemUser systemUser = systemUserService.getOne(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getAccount, account));
        if (systemUser != null) {
            return BeanUtil.copyProperties(systemUser, UniUserDTO.class);
        }
        return null;
    }

    private String decodePassWord(String password) {
        if (StrUtil.isNotBlank(password)) {
            RSA rsa = new RSA(RSAConfig.privateKey, RSAConfig.publicKey);
            byte[] decode = Base64.getDecoder().decode(password);
            byte[] decrypt = rsa.decrypt(decode, KeyType.PrivateKey);
            return new String(decrypt);
        }
        return null;
    }

    @Override
    public LoginUser loginByMiniApp(UserLoginRequest request) {
        UniUserDTO uniUserDTO = null;
        boolean isAdmin = UniClientCodeEnum.ADMIN.getCode().equals(request.getLoginCode());
        if (isAdmin) {
            uniUserDTO = uniUserMiniappMapper.sysUserInfoByOpenId(request.getOpenId());
            if (uniUserDTO != null) {
                return initLoginUser(uniUserDTO, true);
            }
        } else {
            uniUserDTO = uniUserMiniappMapper.orgUserInfoByOpenId(request.getOpenId());
            // 远程获取账号所对应的机构信息、权限等等
            if (uniUserDTO != null) {
                Long orgId = null;
                LoginUser loginUser = initLoginUser(uniUserDTO, false);
                // 先查询教务系统
                R<LoginInfoDTO> r = logisticsEmployeeService.getLoginInfo(new IdRequest(uniUserDTO.getId()));
                if (r.isOk() && r.getData() != null) {
                    orgId = r.getData().getOrgId();
                } else {
                    // 后查询培训系统
                    R<EmployeeDTO> tr = teachEmployeeService.getLoginInfo(new IdRequest(uniUserDTO.getId()));
                    if (tr.isOk() && tr.getData() != null) {
                        orgId = tr.getData().getOrgId();
                    }
                }
                if (orgId == null) {
                    throw new ServiceException("当前登录用户无机构角色");
                }
                loginUser.setOrgId(orgId);
                return loginUser;
            }
        }
        return null;
    }

    private LoginUser initLoginUser(UniUserDTO uniUserDTO, boolean isAdmin) {
        uniUserDTO.setAccount(AesUtil.decode(uniUserDTO.getAccount()));
        uniUserDTO.setPhone(AesUtil.decode(uniUserDTO.getPhone()));
        LoginUser loginUser = BeanUtil.copyProperties(uniUserDTO, LoginUser.class);
        loginUser.setUserType(isAdmin ? UserConstants.USER_PLATFORM : UserConstants.USER_EMPLOYEE);
        loginUser.setSysUser(BeanUtil.copyProperties(uniUserDTO, SysUser.class, "password"));
        return loginUser;
    }

    @Override
    @Transactional
    public LoginUser bindingByMiniApp(UserBindingRequest request) {
        String openId = request.getOpenId();
        LoginUser loginUser = loginByMiniApp(new UserLoginRequest(openId, request.getLoginCode()));
        if (loginUser == null) {
            String phone = request.getPhone();
            UniUserMiniapp miniapp = null;
            if (UniClientCodeEnum.ADMIN.getCode().equals(request.getLoginCode())) {
                miniapp = uniUserMiniappMapper.sysUserInfoByPhone(AesUtil.encode(phone));
            } else {
                miniapp = uniUserMiniappMapper.orgUserInfoByPhone(AesUtil.encode(phone));
            }
            if (miniapp == null) {
                throw new ServiceException("账号不存在");
            }
            if (Boolean.FALSE.equals(request.getReBind()) && StrUtil.isNotBlank(miniapp.getOpenId())) {
                throw new ServiceException("该手机号已绑定小程序，请勿重复绑定");
            }

            // 限制小程序openId只能绑定一个手机号
            Long count = uniUserMiniappMapper.selectCount(Wrappers.<UniUserMiniapp>lambdaQuery().eq(UniUserMiniapp::getOpenId, openId));
            if (count != null && count > 0) {
                log.error("机构、平台端小程序绑定失败，openId ==> {}", openId);
                throw new ServiceException("该openId已被绑定，请勿重复绑定");
            }

            // 重新绑定：先解除之前的绑定信息
            if (Boolean.TRUE.equals(request.getReBind())) {
                uniUserMiniappMapper.deleteById(miniapp.getId());
            }
            // 添加绑定信息
            miniapp.setOpenId(openId);
            uniUserMiniappMapper.insert(miniapp);

            loginUser = loginByMiniApp(new UserLoginRequest(openId, request.getLoginCode()));
        }
        return loginUser;
    }

    @Override
    public Boolean checkPhoneBindingInfo(String loginCode, String phone) {
        UniUserMiniapp miniapp;
        if (UniClientCodeEnum.ADMIN.getCode().equals(loginCode)) {
            miniapp = uniUserMiniappMapper.sysUserInfoByPhone(AesUtil.encode(phone));
        } else {
            miniapp = uniUserMiniappMapper.orgUserInfoByPhone(AesUtil.encode(phone));
        }
        if (miniapp == null) {
            throw new ServiceException("账号不存在");
        }
        if (StrUtil.isNotBlank(miniapp.getOpenId())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
