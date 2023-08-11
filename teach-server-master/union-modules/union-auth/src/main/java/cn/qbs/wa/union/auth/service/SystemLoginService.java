package cn.qbs.wa.union.auth.service;

import cn.qbs.wa.union.auth.pojo.login.*;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserDTO;
import cn.qbs.wa.teach.common.core.domain.LoginUser;

public interface SystemLoginService {

    LoginUser login(LoginRequest request);

    LoginUser loginBySms(LoginRequest request);

    void checkUser(String account, String action,  String appCode);

    void resetPassword(UserPasswordRequest request);

    LoginUser loginByWechat(SocialLoginRequest request);

    UniUserDTO getExistUser(String account, String appCode);

    Boolean checkEmployeePhoneBindingInfo(String account);

    LoginUser loginByMiniApp(UserLoginRequest request);

    LoginUser bindingByMiniApp(UserBindingRequest request);

    Boolean checkPhoneBindingInfo(String loginCode, String account);
}
