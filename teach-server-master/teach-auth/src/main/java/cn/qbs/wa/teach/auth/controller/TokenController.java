package cn.qbs.wa.teach.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.auth.enumerate.SmsAction;
import cn.qbs.wa.teach.auth.pojo.request.*;
import cn.qbs.wa.teach.auth.service.SysLoginService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialBindingDTO;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.JwtUtils;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.oss.utils.TencentCosUtil;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.common.security.service.TokenService;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.common.sms.constant.SmsConstants;
import cn.qbs.wa.teach.common.sms.service.AliSmsService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**

 *
 * @author vieux
 * @version 1.0
 * @date 2021/10/25 14:03
 */
@Slf4j
@RestController
@Api(tags = "登陆器")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    RedisService redisService;

    @Autowired
    AliSmsService aliSmsService;

    @Resource
    private TencentCosUtil tencentCosUtil;

    @Lazy
    @Autowired
    private WxMpService wxMpService;

    @SneakyThrows
    //@PostMapping("/oauth")
    @ApiOperation("获取第三方平台唯一用户ID")
    public R<?> getOAuthUID(@RequestBody OAuthCodeRequest request) {
        WxOAuth2Service oAuth2Service = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(request.getCode());
        WxOAuth2UserInfo wxUserInfo = oAuth2Service.getUserInfo(accessToken, null);
        return R.ok(wxUserInfo);
    }

    @SneakyThrows
    //@PostMapping("/login-social")
    @ApiOperation("员工登录")
    public R<?> loginBySocial(@RequestBody @Validated SocialLoginRequest request) {
        WxOAuth2Service oAuth2Service = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(request.getCode());
        WxOAuth2UserInfo wxUserInfo = oAuth2Service.getUserInfo(accessToken, null);
        request.setUnionId(wxUserInfo.getUnionId());
        // 用户登录
        LoginUser userInfo = sysLoginService.loginBySocial(request);
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
        return R.ok(tokenService.createToken(userInfo));
    }

    @SneakyThrows
    //@PostMapping("/binding-social")
    @ApiOperation("绑定手机")
    public R<?> bindingSocial(@RequestBody @Validated SocialBindingRequest request) {
        checkCode(request.getPhone(), CacheConstants.BIND_PHONE_CODE, request.getCode());
        WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
        accessToken.setAccessToken(request.getAccessToken());
        accessToken.setOpenId(request.getOpenId());
        WxOAuth2UserInfo wxUserInfo = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service().getUserInfo(accessToken, null);
        SocialBindingDTO socialBindingDTO = new SocialBindingDTO();
        socialBindingDTO.setUid(wxUserInfo.getUnionId());
        socialBindingDTO.setNickName(wxUserInfo.getNickname());
        socialBindingDTO.setHeadImgUrl(wxUserInfo.getHeadImgUrl());
        socialBindingDTO.setAccount(request.getPhone());
        // 用户登录
        LoginUser userInfo = sysLoginService.bindingSocial(socialBindingDTO);
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    //@PostMapping("/send-sms-check")
    @ApiOperation("短信发送-验证手机号")
    public R<?> checkPhone(@RequestBody @Validated SendSmsRequest request) {
        String account = request.getAccount();
        String action = request.getAction();
        if (SmsAction.BIND_PHONE.getText().equals(action)) {
            boolean hasBind = sysLoginService.checkPhoneBindingInfo(account);
            if (hasBind) {
                return R.fail("该手机号已绑定其他微信，请更换其他手机号进行绑定，或直接使用该手机号验证码登录");
            }
            String code = aliSmsService.sendVerCodeMsg(request.getAccount(), SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(getCacheKey(CacheConstants.BIND_PHONE_CODE, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
            return R.ok();
        }
        return sendSms(request);
    }

    private String getCacheKey(String prefix, String account) {
        return prefix + ":" + account;
    }

    private void checkCode(String account, String prefix, String code) {
        Object cacheCode = redisService.getCacheObject(getCacheKey(prefix, account));
        if (cacheCode == null) {
            throw new ServiceException("验证码已过期请重新再试");
        }
        String c = cacheCode.toString();
        if (!c.equals(code)) {
            throw new ServiceException("验证码错误,请重新输入");
        }
    }

    @PostMapping("login")
    @ApiOperation("登录")
    public R<?> login(@RequestBody LoginRequest request) {

        checkCaptchaCode(request.getRequestId(), request.getCode());
        // 用户登录
        LoginUser userInfo = sysLoginService.login(request);
        // 获取登录token

        return R.ok(tokenService.createToken(userInfo));
    }


    @PostMapping("login-sms")
    @ApiOperation("短信登录")
    public R<?> loginByCode(@RequestBody @Validated LoginRequest request) {
        Object cacheCode = redisService.getCacheObject(CacheConstants.LOGIN_CODE + request.getAccount());
        if (cacheCode == null) {
            throw new ServiceException("验证码已过期请重新再试");
        }
        String code = cacheCode.toString();
        if (!code.equals(request.getCode())) {
            throw new ServiceException("验证码错误,请重新输入");
        }
        // 用户登录
        LoginUser userInfo = sysLoginService.loginBySms(request);
        // 获取登录token

        return R.ok(tokenService.createToken(userInfo));
    }

    @PostMapping("logout")
    @ApiOperation("登出")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    @ApiIgnore
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    //@PostMapping("send-sms")
    @ApiOperation("短信发送")
    public R<?> sendSms(@RequestBody @Validated SendSmsRequest request) {
        sysLoginService.checkUser(request.getAccount(), request.getAction());
        String code = aliSmsService.sendVerCodeMsg(request.getAccount(), SmsConstants.CODE_LENGTH);
        if (SmsAction.Login.getText().equals(request.getAction())) {
            redisService.setCacheObject(CacheConstants.LOGIN_CODE + request.getAccount(), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        } else if (SmsAction.FORGET_PWD.getText().equals(request.getAction())) {
            redisService.setCacheObject(CacheConstants.FORGET_PWD_CODE + request.getAccount(), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        } else if (SmsAction.CHANGE_PHONE.getText().equals(request.getAction())) {
            redisService.setCacheObject(CacheConstants.CHANGE_PHONE_CODE + request.getAccount(), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        } else if (SmsAction.REGISTER.getText().equals(request.getAction())) {
            redisService.setCacheObject(CacheConstants.REGISTER_PHONE_CODE + request.getAccount(), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        }
        return R.ok();
    }

    //@PostMapping("reset-password")
    @ApiOperation("修改密码")
    public R<?> resetPassword(@RequestBody @Validated UserPasswordRequest request) {
        Object cacheCode = redisService.getCacheObject(CacheConstants.FORGET_PWD_CODE + request.getAccount());
        if (cacheCode == null) {
            throw new ServiceException("验证码已过期请重新再试");
        }
        String code = cacheCode.toString();
        if (!code.equals(request.getCode())) {
            throw new ServiceException("验证码错误,请重新输入");
        }
        sysLoginService.resetPassword(request);
        return R.ok();
    }

    @ApiOperation(value = "腾讯云授权 COS API 临时密钥")
    @PostMapping("/cosCredential")
    public R<JSONObject> getCosCredential(@RequestBody CosCredentialTypeRequest type) {
        String cosType = Optional.ofNullable(type).orElseGet(CosCredentialTypeRequest::new).getType();
        if (StringUtils.isEmpty(cosType)) {
            cosType = TencentCosUtil.UPLOAD_TYPE_ALL;
        }
        String cacheKey = CacheConstants.COS_CREDENTIAL_KEY + cosType;
        String credential = this.redisService.getCacheObject(cacheKey);
        if (StringUtils.isBlank(credential)) {
            credential = tencentCosUtil.getCosCredential(cosType);
        }
        this.redisService.setCacheObject(cacheKey, credential, 7000L, TimeUnit.SECONDS);
        return R.ok(JSONObject.parseObject(credential));
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


    @ApiOperation(value = "获取图形验证码")
    //@PostMapping("/getCaptcha")
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


    @PostMapping("/login-access")
    @ApiOperation("获取用户访问令牌")
    public R<?> accessToken(@RequestBody AccessTokenRequest accessToken, HttpServletRequest request) {

        String token = SecurityUtils.getToken(request);
        if (StringUtils.isEmpty(token)) {
            token = accessToken.getAccessToken();
        }
        if (StringUtils.isEmpty(token)) {
            return R.fail("缺少令牌");
        }

        LoginUser loginUser = AuthUtil.getLoginUser(token);
        if (loginUser == null) {
            return R.fail("登录状态已过期");
        }
        Map<String, Object> rspMap = new HashMap<>(8);
        rspMap.put("access_token", token);
        rspMap.put("expires_in", loginUser.getExpireTime());
        rspMap.put("is_admin", loginUser.getIsAdmin());
        rspMap.put("real_name", loginUser.getUsername());
        rspMap.put("user_id", loginUser.getUserid());
        rspMap.put("user_account", loginUser.getSysUser().getAccount());
        rspMap.put("roles", loginUser.getRoles());
        rspMap.put("head_img_url", loginUser.getHeadImgUrl());
        return R.ok(rspMap);
    }


}
