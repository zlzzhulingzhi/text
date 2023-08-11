package cn.qbs.wa.teach.auth.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.qbs.wa.teach.auth.pojo.request.*;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.RsaUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.*;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class StudentLoginService {

    @Autowired
    RemoteStudentService remoteStudentService;

    @Autowired
    RedisService redisService;


    private Long checkForbidden(String account) {
        Object forbiddenCount = redisService.getCacheObject(CacheConstants.FORIBDDEN_CODE + account);
        if (forbiddenCount == null) {
            redisService.setCacheObject(CacheConstants.FORIBDDEN_CODE + account, Constants.DB_FAIL, CacheConstants.FORIBDDEN_TIME, TimeUnit.MINUTES);
        } else {
            long forbiddenLimit = Long.parseLong(forbiddenCount.toString());
            if (forbiddenLimit >= CacheConstants.FORIBDDEN_COUNT) {
                throw new ServiceException("该账号已停用,请1小时后重新尝试");
            }
            return forbiddenLimit;
        }
        return Long.valueOf(Constants.DB_FAIL);
    }

    /**
     * 验证手机号用户是否存在于系统
     *
     * @param orgId   机构
     * @param account 账号
     * @return 用户信息
     */
    public LoginUser checkUser(Long orgId, String account) {
        R<LoginUser> userResult = remoteStudentService.getLoginInfo(new LoginInfoDTO(orgId, account));
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        return userResult.getData();
    }

    public LoginUser loginBySocial(SocialLoginRequest request) {
        SocialLoginInfoDTO socialLoginInfoDTO = BeanUtil.copyProperties(request, SocialLoginInfoDTO.class);
        R<LoginUser> userResult = remoteStudentService.getSocialLoginInfo(socialLoginInfoDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        return userResult.getData();
    }

    public LoginUser bindingSocial(SocialBindingRequest request, WxOAuth2UserInfo wxUserInfo) {
        SocialBindingDTO socialLoginInfoDTO = new SocialBindingDTO();
        socialLoginInfoDTO.setAccount(request.getPhone());
        socialLoginInfoDTO.setPhone(request.getPhone());
        socialLoginInfoDTO.setOrgId(request.getOrgId());
        socialLoginInfoDTO.setUnionId(wxUserInfo.getUnionId());
        socialLoginInfoDTO.setHeadImgUrl(wxUserInfo.getHeadImgUrl());
        socialLoginInfoDTO.setRealName(wxUserInfo.getNickname());
        socialLoginInfoDTO.setNickName(wxUserInfo.getNickname());
        R<LoginUser> userResult = remoteStudentService.bindingSocial(socialLoginInfoDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        return userResult.getData();
    }

    public boolean checkPhoneBindingInfo(Long orgId, String account) {
        R<Boolean> r = remoteStudentService.getPhoneBindingInfo(new LoginInfoDTO(orgId, account));
        if (R.FAIL == r.getCode()) {
            throw new ServiceException(r.getMsg());
        }
        return r.getData();
    }

    public LoginUser login(Long orgId, Long userId) {
        R<LoginUser> r = remoteStudentService.loginByUserID(new IdOrgRequest(userId, orgId));
        if (!r.isOk()) {
            throw new ServiceException(r.getMsg());
        }
        LoginUser loginUser = r.getData();
        if (loginUser == null) {
            throw new ServiceException("登录用户不存在");
        }
        return loginUser;
    }

    public LoginUser login(StudentLoginRequest request) {
        String account = request.getAccount();
        String password = request.getPassword();
        Long orgId = request.getOrgId();
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(account, password)) {
            recordLoginInfo(account, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        password=decodePassWord(password);
        // 查询用户信息
        LoginUser loginUser = checkUser(orgId, account);
        if (loginUser == null) {
            throw new ServiceException("登录用户：" + account + " 不存在");
        }
        SysUser user = loginUser.getSysUser();
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            recordLoginInfo(account, Constants.LOGIN_FAIL, "用户密码错误");
            throw new ServiceException("密码错误");
        }
        recordLoginInfo(account, Constants.LOGIN_SUCCESS, "登录成功");
        return loginUser;
    }

    public LoginUser loginBySms(StudentLoginRequest request) {
        String account = request.getAccount();
        Long orgId = request.getOrgId();
        // 查询用户信息
        LoginUser loginUser = checkUser(orgId, account);
        if (loginUser == null) {
            // 首次登录将自动注册该机构的学员账号
            StudentRegisterRequest registerRequest = new StudentRegisterRequest();
            registerRequest.setOrgId(request.getOrgId());
            registerRequest.setAccount(account);
            registerRequest.setPhone(account);
            registerRequest.setRealName("学员" + PhoneUtil.subAfter(account));
            loginUser = this.register(registerRequest);
        }
        recordLoginInfo(account, Constants.LOGIN_SUCCESS, "登录成功");
        return loginUser;
    }

    /**
     * 注册
     */
    public LoginUser register(StudentRegisterRequest request) {
        String account = request.getAccount();
        String password = request.getPassword();
        Long orgId = request.getOrgId();
        // 注册用户信息
        StudentDTO studentDTO = BeanUtil.copyProperties(request, StudentDTO.class);
        R<LoginUser> registerResult = remoteStudentService.register(studentDTO);
        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
        return registerResult.getData();
    }

    public void resetPassword(StudentPasswordRequest request) {
        StudentPasswordDTO userPasswordDTO = BeanUtil.copyProperties(request, StudentPasswordDTO.class);
        R<Boolean> r = remoteStudentService.resetPassword(userPasswordDTO);
        if (R.FAIL == r.getCode()) {
            throw new ServiceException(r.getMsg());
        }
    }

    private String decodePassWord(String password) {
        return RsaUtil.decodePassWord(password);
    }

    public boolean existAccount(Long orgId, String account) {
        LoginUser loginUser = checkUser(orgId, account);
        return loginUser != null;
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    private void recordLoginInfo(String username, String status, String message) {

    }
}
