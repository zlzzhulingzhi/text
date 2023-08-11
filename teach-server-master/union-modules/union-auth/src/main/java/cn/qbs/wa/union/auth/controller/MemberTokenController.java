package cn.qbs.wa.union.auth.controller;

import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.union.auth.pojo.login.MiniAppBindingRequest;
import cn.qbs.wa.union.auth.pojo.login.MiniAppLoginRequest;
import cn.qbs.wa.union.auth.service.TokenService;
import cn.qbs.wa.union.auth.service.impl.MemberLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "学员-登陆器")
@Slf4j
@RestController
@RequestMapping("/member")
public class MemberTokenController {

    @Autowired
    private TokenService tokenService;

    @Resource
    private MemberLoginService memberLoginService;

    @PostMapping("/login-miniapp")
    @ApiOperation("学员登录-小程序")
    public R<?> loginByMiniApp(@RequestBody @Validated MiniAppLoginRequest request) {
        // 用户登录
        LoginUser userInfo = memberLoginService.loginByMiniApp(request.getOpenId());
        if (userInfo != null) {
            // 获取登录token
            userInfo.setToken(request.getOpenId());
            return R.ok(tokenService.createTokenForMember(userInfo));
        }
        return R.ok(null);
    }

    @PostMapping("/binding-miniapp")
    @ApiOperation("账号绑定-小程序")
    public R<?> bindingMiniApp(@RequestBody @Validated MiniAppBindingRequest request) {
        // 用户登录
        LoginUser userInfo = memberLoginService.bindingByMiniApp(request);
        // 获取登录token
        userInfo.setToken(request.getOpenId());
        return R.ok(tokenService.createTokenForMember(userInfo));
    }

}
