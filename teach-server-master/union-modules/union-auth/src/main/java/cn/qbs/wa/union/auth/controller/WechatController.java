package cn.qbs.wa.union.auth.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.union.auth.pojo.login.OAuthCodeRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2022/7/12 11:40
 */

@Api(tags = "微信登陆器")
@RestController
@RequestMapping("/wc")
public class WechatController {

    @Lazy
    @Resource
    private WxMaService wxMaService;

    @SneakyThrows
    @PostMapping("/miniapp-info")
    @ApiOperation("获取用户微信小程序信息")
    public R<?> getMiniAppInfo(@RequestBody @Validated OAuthCodeRequest request) {
        WxMaJscode2SessionResult sessionInfo = wxMaService.switchoverTo(request.getAppid()).getUserService().getSessionInfo(request.getCode());
        sessionInfo.setSessionKey(null);
        return R.ok(sessionInfo);
    }

    @SneakyThrows
    @PostMapping("/miniapp-phone")
    @ApiOperation("获取用户微信小程序绑定手机号")
    public R<?> getMiniAppPhone(@RequestBody @Validated OAuthCodeRequest request) {
        WxMaPhoneNumberInfo phoneInfo = wxMaService.switchoverTo(request.getAppid()).getUserService().getNewPhoneNoInfo(request.getCode());
        if (phoneInfo == null) {
            return R.fail("授权码不正确");
        }
        return R.ok(phoneInfo.getPurePhoneNumber());
    }

/*
    @Lazy
    @Autowired
    WxMpService wxMpService;

    @Autowired
    SystemLoginService sysLoginService;

    @Autowired
    TokenService tokenService;

    @Autowired
    RedisService redisService;

    @Autowired
    UniUserOauthService uniUserOauthService;

    @Autowired
    AliSmsService aliSmsService;


    @SneakyThrows
    @PostMapping("/oauth")
    @ApiOperation("获取第三方平台唯一用户ID")
    public R<?> getOAuthUID(@RequestBody OAuthCodeRequest request) {
        WxOAuth2Service oAuth2Service = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(request.getCode());
        WxOAuth2UserInfo wxUserInfo = oAuth2Service.getUserInfo(accessToken, null);
        return R.ok(wxUserInfo);
    }

    @SneakyThrows
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public R<?> loginBySocial(@RequestBody @Validated SocialLoginRequest request) {
        WxOAuth2Service oAuth2Service = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service();
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(request.getCode());
        WxOAuth2UserInfo wxUserInfo = oAuth2Service.getUserInfo(accessToken, null);
        request.setUnionId(wxUserInfo.getUnionId());
        // 用户登录
        LoginUser userInfo = sysLoginService.loginByWechat(request);
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
    @PostMapping("/binding")
    @ApiOperation("绑定手机")
    public R<?> bindingSocial(@RequestBody @Validated WechatBindingRequest request) {
        checkCode(request.getPhone(), CacheConstants.BIND_PHONE_CODE, request.getCode());
        WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
        accessToken.setAccessToken(request.getAccessToken());
        accessToken.setOpenId(request.getOpenId());
        WxOAuth2UserInfo wxUserInfo = wxMpService.switchoverTo(request.getAppid()).getOAuth2Service().getUserInfo(accessToken, null);
        // 用户登录
        UniUserDTO existUser = sysLoginService.getExistUser(request.getPhone(), request.getLoginCode());
        if (existUser == null) {
            throw new ServiceException("不存在该用户");
        }
        LoginUser userInfo = uniUserOauthService.bindingByWechat(request, wxUserInfo, existUser);
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }


    @SneakyThrows
    @PostMapping("/unbind")
    @ApiOperation("解绑手机")
    public R<?> unbind(@RequestBody @Validated WechatUnBindingRequest request) {
        uniUserOauthService.unBindingByWechat(request);
        return R.ok();
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

    private String getCacheKey(String prefix, String requestId) {
        return prefix + requestId;
    }

    @PostMapping("/send-sms-check")
    @ApiOperation("短信发送-验证手机号")
    public R<?> checkPhone(@RequestBody @Validated SendSmsRequest request) {
        String account = request.getAccount();
        String action = request.getAction();
        if (SmsAction.BIND_PHONE.getText().equals(action)) {
            Boolean hasBind = sysLoginService.checkEmployeePhoneBindingInfo(account);
            if (hasBind) {
                return R.fail("该手机号已绑定其他微信，请更换其他手机号进行绑定，或直接使用该手机号验证码登录");
            }
            String code = aliSmsService.sendVerCodeMsg(request.getAccount(), SmsConstants.CODE_LENGTH);
            redisService.setCacheObject(getCacheKey(CacheConstants.BIND_PHONE_CODE, account), code, SmsConstants.EXPIRE_MINUTE, TimeUnit.MINUTES);
            return R.ok();
        }
        return R.fail();
    }


    @ApiOperation("绑定微信账号")
    @PostMapping("/binding-social")
    R bindingSocial(@RequestBody UserOauthBindingRequest request) {
        request.setUserId(SecurityContextHolder.getUserId());
        this.uniUserOauthService.bindingSocial(request);
        return R.ok();
    }

    @PostMapping("/unbind-social")
    @ApiOperation("解除绑定")
    public R<Boolean> unbindSocial() {
        return R.ok(this.uniUserOauthService.unbindSocial(SecurityContextHolder.getUserId()));
    }

    @ApiOperation("绑定关系校验")
    @PostMapping("/binding-check")
    R<Boolean> checkPhoneBind(@RequestBody UserAccountRequest request) {
        request.setUserId(SecurityContextHolder.getUserId());
        return R.ok(this.uniUserOauthService.checkBind(request));
    }
*/


}
