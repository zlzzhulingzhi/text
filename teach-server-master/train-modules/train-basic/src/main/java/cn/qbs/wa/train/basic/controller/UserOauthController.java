package cn.qbs.wa.train.basic.controller;

import cn.qbs.wa.train.basic.entity.User;
import cn.qbs.wa.train.basic.pojo.useroauth.OauthAccountRequest;
import cn.qbs.wa.train.basic.pojo.useroauth.OauthInfoResponse;
import cn.qbs.wa.train.basic.pojo.useroauth.UserAccountRequest;
import cn.qbs.wa.train.basic.pojo.useroauth.UserOauthBindingRequest;
import cn.qbs.wa.train.basic.service.UserOauthService;
import cn.qbs.wa.train.basic.service.UserService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户绑定微信关系表(UserOauth)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
@Api(tags = "微信授权")
@RestController
@RequestMapping("/userOauth")
public class UserOauthController {

    @Resource
    private UserOauthService userOauthService;

    @Resource
    private UserService userService;

    @ApiOperation("获取登录授权")
    @PostMapping("/login-social")
    R<LoginUser> getUserInfo(@RequestBody OauthAccountRequest request) {
        User user = this.userOauthService.getUserInfo(request);
        return R.ok(userService.getLoginUser(user));
    }

    @ApiOperation("获取个人信息")
    @PostMapping("/info")
    R<OauthInfoResponse> getUserInfo() {
        return R.ok(this.userOauthService.getUserInfo(SecurityContextHolder.getUserId()));
    }

    @ApiOperation("绑定微信账号")
    @PostMapping("/binding-social")
    R<LoginUser> bindingSocial(@RequestBody UserOauthBindingRequest request) {
        User user = this.userOauthService.bindingSocial(request);
        return R.ok(initLoginUser(user));
    }

    @PostMapping("/unbind-social")
    @ApiOperation("解除绑定")
    public R<Boolean> unbindSocial() {
        return R.ok(this.userOauthService.unbindSocial(SecurityContextHolder.getUserId()));
    }

    @ApiOperation("绑定关系校验")
    @PostMapping("/binding-check")
    R<Boolean> checkPhoneBind(@RequestBody UserAccountRequest request) {
        return R.ok(this.userOauthService.checkPhoneBind(request));
    }

    private LoginUser initLoginUser(User user) {
        if (user == null) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(user.getId());
        loginUser.setUsername(user.getRealName());
        loginUser.setOrgId(Constants.INIT_ORG);
        loginUser.setHeadImgUrl(user.getHeadImgUrl());
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        sysUser.setUserName(user.getRealName());
        sysUser.setUserId(user.getId());
        sysUser.setOrgId(Constants.INIT_ORG);
        loginUser.setSysUser(sysUser);
        return loginUser;
    }
}

