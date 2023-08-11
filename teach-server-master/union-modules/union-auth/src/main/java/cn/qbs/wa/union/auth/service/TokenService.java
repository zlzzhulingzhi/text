package cn.qbs.wa.union.auth.service;

import cn.qbs.wa.teach.common.core.domain.LoginUser;

import java.util.Map;

public interface TokenService {

    Map<String, Object> createToken(LoginUser userInfo);

    Map<String, Object> createTokenForMember(LoginUser userInfo);
}
