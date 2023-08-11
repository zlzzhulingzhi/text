package cn.qbs.wa.union.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.enums.UniClientCodeEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.JwtUtils;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.common.sms.constant.SmsConstants;
import cn.qbs.wa.teach.common.sms.service.AliSmsService;
import cn.qbs.wa.union.auth.enumerate.PhoneAcquireModeEnum;
import cn.qbs.wa.union.auth.enumerate.SmsAction;
import cn.qbs.wa.union.auth.mapper.UniUserMiniappMapper;
import cn.qbs.wa.union.auth.pojo.login.*;
import cn.qbs.wa.union.auth.pojo.uniorg.UniOrg;
import cn.qbs.wa.union.auth.service.SystemLoginService;
import cn.qbs.wa.union.auth.service.TokenService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2022/7/8 15:41
 */

@Api(tags = "登陆器")
@RestController
@RequestMapping("/index")
public class IndexController {


    @Autowired
    RedisService redisService;

    @Autowired
    AliSmsService aliSmsService;

    @Autowired
    SystemLoginService sysLoginService;

    @Autowired
    UniUserMiniappMapper uniUserMiniappMapper;

    @Autowired
    TokenService tokenService;


    @PostMapping("login")
    @ApiOperation("登录")
    public R<?> login(@RequestBody LoginRequest request) {
        checkCaptchaCode(request.getRequestId(), request.getCode());
        // 用户登录
        LoginUser userInfo = sysLoginService.login(request);
        // 获取登录token

        return R.ok(tokenService.createToken(userInfo));
    }

    //@PostMapping("login-sms")
    @ApiOperation("短信登录")
    public R<?> loginSms(@RequestBody LoginRequest request) {
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

        return R.ok(tokenService.createToken(userInfo));
    }


    //@PostMapping("token-info")
    @ApiOperation("token信息")
    public R<?> checkToken(@RequestBody @Validated TokenInfoRequest request) {
        String token = request.getToken();
        if (StringUtils.isEmpty(token)) {
            throw new ServiceException("令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            throw new ServiceException("令牌已过期或验证不正确");

        }
        String userKey = JwtUtils.getUserKey(claims);
        String tokenKey = getTokenKey(userKey);
        boolean islogin = redisService.hasKey(tokenKey);
        if (!islogin) {
            throw new ServiceException("登录状态已过期");
        }

        TokenInfoResponse tokenInfoResponse = new TokenInfoResponse();
        tokenInfoResponse.setUserId(Long.valueOf(JwtUtils.getUserId(claims)));
        tokenInfoResponse.setUserName(JwtUtils.getUserName(claims));
        tokenInfoResponse.setUserKey(userKey);

        return R.ok(tokenInfoResponse);
    }

    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    @ApiOperation(value = "获取图形验证码")
    @PostMapping("/")
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

    @PostMapping("send-sms")
    @ApiOperation("短信发送")
    public R<?> sendSms(@RequestBody @Validated SendSmsRequest request) {
        sysLoginService.checkUser(request.getAccount(), request.getAction(), request.getLoginCode());
        if (SmsAction.Login.getText().equals(request.getAction())) {
            String code = aliSmsService.sendVerCodeMsg(request.getAccount(), SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(CacheConstants.LOGIN_CODE + request.getAccount(), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        } else if (SmsAction.FORGET_PWD.getText().equals(request.getAction())) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser == null) {
                return R.fail("没有访问权限");
            }
            // 校验当前登录用户手机号，与发送验证码手机号是否一致
            if (!request.getAccount().equals(loginUser.getAccount())) {
                return R.fail("发送验证码手机号与当前登录人手机号不一致");
            }
            String code = aliSmsService.sendVerCodeMsg(request.getAccount(), SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(CacheConstants.FORGET_PWD_CODE + request.getAccount(), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
        }
        return R.ok();
    }

    @PostMapping("reset-password")
    @ApiOperation("修改密码")
    public R<?> resetPassword(@RequestBody @Validated UserPasswordRequest request) {
        sysLoginService.resetPassword(request);
        // 修改密码成功后,需要重新登录
        AuthUtil.logout();
        return R.ok();
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

    private String getCaptchaCacheKey(String prefix, String requestId) {
        return prefix + requestId;
    }

    @PostMapping("/login-miniapp")
    @ApiOperation("后台登录-小程序")
    public R<?> loginByMiniApp(@RequestBody @Validated UserLoginRequest request) {
        // 用户登录
        LoginUser userInfo = sysLoginService.loginByMiniApp(request);
        if (userInfo == null) {
            R<Map<String, String>> r = new R<>();
            r.setCode(201);
            return r;
        }
        // 获取登录token
        userInfo.setToken(request.getOpenId());
        return tokenMap(userInfo, request.getLoginCode());
    }

    @PostMapping("/binding-miniapp")
    @ApiOperation("账号绑定-小程序")
    public R<?> bindingMiniApp(@RequestBody @Validated UserBindingRequest request) {
        if (PhoneAcquireModeEnum.MANUAL.getCode().equals(request.getMode())) {
            // 验证短信验证码
            String code = request.getCode();
            if (StrUtil.isBlank(code)) {
                throw new ServiceException("请输入验证码");
            }
            String captchaCode = this.redisService.getCacheObject(getCaptchaCacheKey(CacheConstants.BIND_PHONE_CODE, request.getPhone()));
            if (!code.equals(captchaCode)) {
                throw new ServiceException("验证码不正确");
            }
            this.redisService.deleteObject(getCaptchaCacheKey(CacheConstants.BIND_PHONE_CODE, request.getPhone()));
        }
        // 用户登录
        LoginUser userInfo = sysLoginService.bindingByMiniApp(request);
        userInfo.setToken(request.getOpenId());
        // 获取登录token
        return tokenMap(userInfo, request.getLoginCode());
    }

    private R<?> tokenMap(LoginUser userInfo, String loginCode) {
        Map<String, Object> tokenMap = tokenService.createToken(userInfo);
        boolean isOrg = UniClientCodeEnum.ORG.getCode().equals(loginCode);
        if (isOrg) {
            UniOrg uniOrg = uniUserMiniappMapper.getOrgInfo(userInfo.getOrgId());
            if (uniOrg == null || Constants.DB_FAIL.equals(uniOrg.getEnabled())) {
                return R.fail("当前登录用户所属机构已被禁用");
            }
            tokenMap.put("orgName", uniOrg.getName());
        }
        return R.ok(tokenMap);
    }

    @PostMapping("/send-sms-check")
    @ApiOperation("短信发送-验证手机号")
    public R<?> checkPhone(@RequestBody @Validated SendSmsRequest request) {
        String phone = request.getAccount();
        String action = request.getAction();
        if (SmsAction.BIND_PHONE.getText().equals(action)) {
            Boolean hasBind = sysLoginService.checkPhoneBindingInfo(request.getLoginCode(), phone);
            if (hasBind) {
                return R.fail("该手机号已绑定其他微信小程序，请更换其他手机号进行绑定");
            }
            String code = aliSmsService.sendVerCodeMsg(request.getAccount(), SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(getCaptchaCacheKey(CacheConstants.BIND_PHONE_CODE, phone), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
            return R.ok();
        }
        return R.fail();
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
        }
        return R.ok();
    }
}
