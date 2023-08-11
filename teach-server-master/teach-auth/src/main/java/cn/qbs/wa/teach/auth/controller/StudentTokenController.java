package cn.qbs.wa.teach.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.auth.enumerate.SmsAction;
import cn.qbs.wa.teach.auth.pojo.request.*;
import cn.qbs.wa.teach.auth.service.StudentLoginService;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.TenantFieldInject;
import cn.qbs.wa.teach.common.security.service.TokenService;
import cn.qbs.wa.teach.common.sms.constant.SmsConstants;
import cn.qbs.wa.teach.common.sms.service.AliSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yjx
 * @version 1.0
 * @date 2022/03/01 14:03
 */
//@RestController
//@Api(tags = "学员登陆器")
//@RequestMapping("/student")
public class StudentTokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StudentLoginService studentLoginService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AliSmsService aliSmsService;

    @Lazy
    @Autowired
    private WxMpService wxMpService;

    @TenantFieldInject
    @SneakyThrows
    @PostMapping("/login-social")
    @ApiOperation("学员登录")
    public R<?> loginBySocial(@RequestBody @Validated SocialLoginRequest request) {
        if (request.getOrgId() == null) {
            return R.fail("机构缺失");
        }
        WxOAuth2Service oAuth2Service = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(request.getCode());
        WxOAuth2UserInfo wxUserInfo = oAuth2Service.getUserInfo(accessToken, null);
        request.setUnionId(wxUserInfo.getUnionId());
        // 用户登录
        LoginUser userInfo = studentLoginService.loginBySocial(request);
        if (userInfo == null) {
            Map<String, String> map = new HashMap<>(2);
            map.put("accessToken", accessToken.getAccessToken());
            map.put("openId", accessToken.getOpenId());
            R<Map<String, String>> r = new R<>();
            r.setCode(201);
            r.setData(map);
            return r;
        }
        // 获取登录token
        return R.ok(tokenService.createTokenForStudent(userInfo));
    }

    @TenantFieldInject
    @SneakyThrows
    @PostMapping("/binding-social")
    @ApiOperation("绑定手机")
    public R<?> bindingSocial(@RequestBody @Validated SocialBindingRequest request) {
        if (request.getOrgId() == null) {
            return R.fail("机构缺失");
        }
        checkCode(request.getOrgId(), request.getPhone(), CacheConstants.BIND_PHONE_CODE, request.getCode());
        WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
        accessToken.setAccessToken(request.getAccessToken());
        accessToken.setOpenId(request.getOpenId());
        WxOAuth2UserInfo wxUserInfo = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service().getUserInfo(accessToken, null);
        // 用户登录
        LoginUser userInfo = studentLoginService.bindingSocial(request, wxUserInfo);
        // 获取登录token
        return R.ok(tokenService.createTokenForStudent(userInfo));
    }

    @TenantFieldInject
    @PostMapping("/login")
    @ApiOperation("学员登录")
    public R<?> login(@RequestBody @Validated StudentLoginRequest request) {

        checkCaptchaCode(request.getRequestId(), request.getCode());

        // 用户登录
        LoginUser userInfo = studentLoginService.login(request);
        // 获取登录token
        return R.ok(tokenService.createTokenForStudent(userInfo));
    }

    @TenantFieldInject
    @PostMapping("/login-sms")
    @ApiOperation("短信登录")
    public R<?> loginBySms(@RequestBody @Validated StudentLoginRequest request) {
        Long orgId = request.getOrgId();
        String account = request.getAccount();
        checkCode(request.getOrgId(), request.getAccount(), CacheConstants.LOGIN_CODE, request.getCode());
        // 用户登录
        LoginUser userInfo = studentLoginService.loginBySms(request);
        redisService.deleteObject(getCacheKey(CacheConstants.LOGIN_CODE, orgId, account));
        // 获取登录token
        return R.ok(tokenService.createTokenForStudent(userInfo));
    }

    @TenantFieldInject
    @PostMapping("/send-sms")
    @ApiOperation("短信发送")
    public R<?> sendSms(@RequestBody @Validated StudentSmsRequest request) {
        Long orgId = request.getOrgId();
        String account = request.getAccount();
        if (!PhoneUtil.isMobile(account)) {
            return R.fail("请输入正确的手机号");
        }
        String action = request.getAction();
        if (SmsAction.Login.getText().equals(action)) {
            String code = aliSmsService.sendVerCodeMsg(account, SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(getCacheKey(CacheConstants.LOGIN_CODE, orgId, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        } else if (SmsAction.BIND_PHONE.getText().equals(action)) {
            String code = aliSmsService.sendVerCodeMsg(account, SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(getCacheKey(CacheConstants.BIND_PHONE_CODE, orgId, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        } else {
            boolean existAccount = studentLoginService.existAccount(orgId, account);
            if (SmsAction.FORGET_PWD.getText().equals(action)) {
                if (!existAccount) {
                    return R.fail("该手机号未注册，无法操作忘记密码。");
                }
                String code = aliSmsService.sendVerCodeMsg(account, SmsConstants.CODE_LENGTH);
                redisService.setCacheObject(getCacheKey(CacheConstants.FORGET_PWD_CODE, orgId, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
            } else if (SmsAction.CHANGE_PHONE.getText().equals(action)) {
                if (existAccount) {
                    return R.fail("该手机号的学员已存在。");
                }
                String code = aliSmsService.sendVerCodeMsg(account, SmsConstants.CODE_LENGTH);
                redisService.setCacheObject(getCacheKey(CacheConstants.CHANGE_PHONE_CODE, orgId, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
            } else if(SmsAction.REGISTER.getText().equals(action)){
                String code = aliSmsService.sendVerCodeMsg(account, SmsConstants.CODE_LENGTH);
                redisService.setCacheObject(getCacheKey(CacheConstants.REGISTER_PHONE_CODE, null, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
            }
        }
        return R.ok();
    }

    @TenantFieldInjectgetDormitoryState
    @PostMapping("/send-sms-check")
    @ApiOperation("短信发送-验证手机号")
    public R<?> checkPhone(@RequestBody @Validated StudentSmsRequest request) {
        Long orgId = request.getOrgId();
        String account = request.getAccount();
        String action = request.getAction();
        if (!PhoneUtil.isMobile(account)) {
            return R.fail("请输入正确的手机号");
        }
        if (SmsAction.BIND_PHONE.getText().equals(action)) {
            boolean hasBind = studentLoginService.checkPhoneBindingInfo(orgId, account);
            if (hasBind) {
                return R.fail("该手机号已绑定其他微信，请更换其他手机号进行绑定，或直接使用该手机号验证码登录");
            }
        }
        return sendSms(request);
    }

    @TenantFieldInject
    @PostMapping("/reset-password")
    @ApiOperation("忘记密码-修改密码")
    public R<?> resetPassword(@RequestBody @Validated StudentPasswordRequest request) {
        Long orgId = request.getOrgId();
        String account = request.getAccount();
        checkCode(orgId, account, CacheConstants.FORGET_PWD_CODE, request.getCode());
        studentLoginService.resetPassword(request);
        redisService.deleteObject(getCacheKey(CacheConstants.FORGET_PWD_CODE, orgId, account));
        return R.ok();
    }

    @TenantFieldInject
    @PostMapping("/register")
    @ApiOperation("学员注册")
    public R<?> register(@RequestBody @Validated StudentRegisterRequest request) {
        Long orgId = request.getOrgId();
        String account = request.getAccount();
        String password = request.getPassword();
        if (orgId == null) {
            return R.fail("机构ID不能为空");
        }
        if (StringUtils.isAnyBlank(account, password)) {
            return R.fail("用户名/密码必须填写");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            return R.fail("密码长度必须在5到20个字符之间");
        }
        LoginUser loginUser = studentLoginService.checkUser(orgId, account);
        if (loginUser != null) {
            return R.fail("用户：" + account + " 已存在");
        }
        checkCode(orgId, account, CacheConstants.BIND_PHONE_CODE, request.getCode());
        // 用户注册
        studentLoginService.register(request);
        return R.ok();
    }

    private String getCacheKey(String prefix, Long orgId, String account) {
        return prefix + ":" + orgId + ":" + account;
    }

    private void checkCode(Long orgId, String account, String cachePrefix, String code) {
        Object cacheCode = redisService.getCacheObject(getCacheKey(cachePrefix, orgId, account));
        if (cacheCode == null) {
            throw new ServiceException("验证码已过期请重新再试");
        }
        String c = cacheCode.toString();
        if (!c.equals(code)) {
            throw new ServiceException("验证码错误,请重新输入");
        }
    }

    @ApiOperation(value = "获取图形验证码")
    @PostMapping("/getCaptcha")
    public void getCaptcha(HttpServletResponse resp, @RequestBody CaptchaRequest request) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(120, 40, 4, 3);
        //得到code
        String code = captcha.getCode();
        redisService.setCacheObject(getCaptchaCacheKey(CacheConstants.CAPTCHA_CODE, request.getRequestId()), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        //输出流
        ServletOutputStream outputStream = resp.getOutputStream();
        resp.setContentType("image/png");
        //读写输出流
        captcha.write(outputStream);
        //关闭输出流
        outputStream.close();
    }

    private String getCaptchaCacheKey(String prefix, String requestId) {
        return prefix + requestId;
    }

    private void checkCaptchaCode(String requestId, String code) {
        if (StrUtil.isBlank(code)) {
            throw new ServiceException("请输入验证码");
        }
        String captchaCode = this.redisService.getCacheObject(getCaptchaCacheKey(CacheConstants.CAPTCHA_CODE, requestId));
        if (!code.equals(captchaCode)) {
            throw new ServiceException("验证码输入错误");
        }

    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/getAccessToken")
    public R<Map<String, Object>> getAccessToken(@RequestBody @Validated ExchangeTokenRequest request) {
        if (request.getOrgId() == null) {
            return R.fail("机构ID缺失");
        }
        if (request.getMemberId() == null) {
            return R.fail("用户ID缺失");
        }

        String key = StrUtil.isBlank(request.getKey()) ? UUID.randomUUID().toString() : request.getKey();
        String userKey = request.getOrgId() + "-" + key;
        LoginUser loginUser = tokenService.getLoginUserByKey(userKey);
        if (loginUser == null) {
            loginUser = studentLoginService.login(request.getOrgId(), request.getMemberId());
            loginUser.setUserid(request.getMemberId());
            loginUser.setToken(userKey);
        }

        Map<String, Object> map = tokenService.createTokenForStudent(loginUser);
        map.put("domain", loginUser.getDomain());
        return R.ok(map);
    }

}
