package cn.qbs.wa.union.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.union.auth.mapper.MemberLoginMapper;
import cn.qbs.wa.union.auth.pojo.login.MiniAppBindingRequest;
import cn.qbs.wa.union.auth.pojo.member.MemberInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class MemberLoginService {

    @Resource
    private MemberLoginMapper memberLoginMapper;

    /**
     * 学员登录
     * @param openId 小程序用户ID
     * @return 登录信息
     */
    public LoginUser loginByMiniApp(String openId) {
        MemberInfo memberInfo = memberLoginMapper.getByOpenId(openId);
        LoginUser loginUser = null;
        if (memberInfo != null) {
            loginUser = new LoginUser();
            loginUser.setUserid(memberInfo.getMemberId());
            loginUser.setAccount(AesUtil.decode(memberInfo.getAccount()));
            loginUser.setUsername(memberInfo.getRealName());
            loginUser.setUserType(UserConstants.USER_STUDENT);
            loginUser.setHeadImgUrl(memberInfo.getHeadImgUrl());
            SysUser sysUser = new SysUser();
            sysUser.setSex(Optional.ofNullable(memberInfo.getSex()).orElse(0).toString());
            loginUser.setSysUser(sysUser);
        }
        return loginUser;
    }

    /**
     * 学员绑定小程序
     * @param request 小程序用户信息
     * @return 登录信息
     */
    public LoginUser bindingByMiniApp(MiniAppBindingRequest request) {
        LoginUser loginUser = this.loginByMiniApp(request.getOpenId());
        String phone = request.getPhone();
        if (loginUser != null) {
            if (loginUser.getAccount().equals(phone)) {
                return loginUser;
            } else {
                throw new ServiceException("该小程序用户已绑定手机号，请勿重复绑定");
            }
        }
        String encode = AesUtil.encode(phone);
        MemberInfo memberInfo = memberLoginMapper.getByAccount(encode);
        if (memberInfo != null && StrUtil.isNotBlank(memberInfo.getOpenId())) {
            throw new ServiceException("该手机号已绑定小程序，请勿重复绑定");
        }
        if (memberInfo == null) {
            memberInfo = new MemberInfo();
            memberInfo.setAccount(encode);
            memberInfo.setPhone(encode);
            memberInfo.setOpenId(request.getOpenId());
            memberInfo.setRealName(request.getRealName());
            memberLoginMapper.insert(memberInfo);
        } else {
            memberInfo.setOpenId(request.getOpenId());
            memberLoginMapper.updateByMemberId(memberInfo);
        }
        loginUser = new LoginUser();
        loginUser.setUserid(memberInfo.getMemberId());
        loginUser.setAccount(phone);
        loginUser.setUsername(memberInfo.getRealName());
        return loginUser;
    }
}
