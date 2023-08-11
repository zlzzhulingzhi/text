package cn.qbs.wa.teach.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.qbs.wa.teach.auth.enumerate.SmsAction;
import cn.qbs.wa.teach.auth.pojo.request.*;
import cn.qbs.wa.teach.basic.api.RemoteUserOauthService;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialBindingDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialInfoDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserInfoDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserPasswordDTO;
import cn.qbs.wa.teach.common.core.config.RSAConfig;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeeRegisterDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {

    @Autowired
    RemoteUserService remoteUserService;
    @Autowired
    RemoteUserOauthService remoteUserOauthService;

    @Autowired
    RemoteEmployeeService remoteEmployeeService;

    @Autowired
    RedisService redisService;

    public LoginUser loginBySocial(SocialLoginRequest request) {
        SocialInfoDTO socialInfoDTO = new SocialInfoDTO(request.getUnionId());
        R<LoginUser> userResult = remoteUserOauthService.getSocialLoginInfo(socialInfoDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        return userResult.getData();
    }

    public LoginUser bindingSocial(SocialBindingDTO socialBindingDTO) {
        R<LoginUser> userResult = remoteUserOauthService.bindingSocial(socialBindingDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        return userResult.getData();
    }

    public boolean checkPhoneBindingInfo(String account) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccount(account);
        R<Boolean> r = remoteUserOauthService.getPhoneBindingInfo(userInfoDTO);
        if (R.FAIL == r.getCode()) {
            throw new ServiceException(r.getMsg());
        }
        return r.getData();
    }

    /**
     * 登录
     */
    public LoginUser login(LoginRequest request) {

        String account = request.getAccount();
        String password = request.getPassword();
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(account, password)) {
            recordLogininfo(account, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        password = decodePassWord(password);
        Long forbiddenLimit = checkForbidden(request.getAccount());
        // 查询用户信息
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccount(account);
        R<LoginUser> userResult = remoteUserService.getLoginInfo(userInfoDTO);


        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLogininfo(account, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + account + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();

        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            recordLogininfo(account, Constants.LOGIN_FAIL, "用户密码错误");
            redisService.setCacheObject(CacheConstants.FORIBDDEN_CODE + account, forbiddenLimit+Constants.DB_TRUE, CacheConstants.FORIBDDEN_TIME, TimeUnit.MINUTES);
            throw new ServiceException("用户不存在/密码错误");
        }
        recordLogininfo(account, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }


    private Long checkForbidden(String account) {
        Object forbiddenCount = redisService.getCacheObject(CacheConstants.FORIBDDEN_CODE + account);
        if (forbiddenCount == null) {
            redisService.setCacheObject(CacheConstants.FORIBDDEN_CODE + account, Constants.DB_FAIL, CacheConstants.FORIBDDEN_TIME, TimeUnit.MINUTES);
        } else {
            Long forbiddenLimit = Long.valueOf(forbiddenCount.toString());
            if (forbiddenLimit >= CacheConstants.FORIBDDEN_COUNT) {
                throw new ServiceException("该账号已停用,请1小时后重新尝试");
            }
            return forbiddenLimit;
        }
        return Long.valueOf(Constants.DB_FAIL);
    }


    public void checkUser(String account, String action) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccount(account);
        R<LoginUser> userResult = remoteUserService.getLoginInfo(userInfoDTO);
        if (SmsAction.REGISTER.getText().equals(action)) {
            if (R.SUCCESS == userResult.getCode() && userResult.getData() != null) {
                // 注册用户检查用户存在，则提示用户已存在
                throw new ServiceException("手机号：" + account + " 用户被注册");
            } else if (R.FAIL == userResult.getCode()) {
                throw new ServiceException(userResult.getMsg());
            }
            return;
        }
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        if (StringUtils.isNull(userResult.getData())) {
            recordLogininfo(account, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + account + " 不存在");
        }
    }


    public LoginUser loginBySms(LoginRequest request) {
        checkForbidden(request.getAccount());
        String account = request.getAccount();
        // 查询用户信息
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccount(account);
        R<LoginUser> userResult = remoteUserService.getLoginInfo(userInfoDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLogininfo(account, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + account + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        recordLogininfo(account, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }


    public void logout(String loginName) {
        recordLogininfo(loginName, Constants.LOGOUT, "退出成功");
    }

    /**
     * 用户注册
     */
    public void register(RegisterRequest request) {

        String username = request.getAccount();

        String password = request.getPassword();

        Long orgId = request.getOrgId();

        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(username);
        userDTO.setNickName(username);
        userDTO.setPassword(SecurityUtils.encryptPassword(password));
        userDTO.setOrgId(orgId);
        R<?> registerResult = remoteUserService.registerUser(userDTO);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
    }

    /**
     * 职员注册
     */
    public void registerEmployee(EmployeeRegisterRequest request) {
        String username = request.getAccount();

        String password = request.getPassword();
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户名/密码必须填写");
        }
        // 注册职员信息
        EmployeeRegisterDTO registerDTO = BeanUtil.copyProperties(request, EmployeeRegisterDTO.class);
        R<?> registerResult = remoteEmployeeService.register(registerDTO);
        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    public void recordLogininfo(String username, String status, String message) {

    }

    public void resetPassword(UserPasswordRequest request) {
        request.setPassword(decodePassWord(request.getPassword()));
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        BeanUtils.copyProperties(request, userPasswordDTO);
        remoteUserService.resetPassword(userPasswordDTO);
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

    public LoginUser loginByToken(Long userId) {
        // 查询用户信息
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(userId);
        R<LoginUser> userResult = remoteUserService.getLoginInfo(userInfoDTO);

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            throw new ServiceException("登录用户不存在");
        }
        LoginUser userInfo = userResult.getData();
        return userInfo;
    }
}
