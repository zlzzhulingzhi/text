package cn.qbs.wa.union.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.utils.IpUtils;
import cn.qbs.wa.teach.common.core.utils.JwtUtils;
import cn.qbs.wa.teach.common.core.utils.ServletUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.union.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2022/7/8 15:47
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;



    @Override
    public Map<String, Object> createToken(LoginUser loginUser) {
        String token = StrUtil.isNotBlank(loginUser.getToken()) ? loginUser.getToken() : UUID.randomUUID().toString();
        Long userId = loginUser.getUserid();
        String userName = loginUser.getUsername();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        // 员工用户 token
        claimsMap.put(SecurityConstants.USER_TYPE, StrUtil.isBlank(loginUser.getUserType()) ? UserConstants.USER_EMPLOYEE : loginUser.getUserType());
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        rspMap.put("real_name", loginUser.getSysUser().getUserName());
        rspMap.put("nick_name", loginUser.getSysUser().getNickName());
        rspMap.put("user_id", loginUser.getSysUser().getUserId());
        rspMap.put("user_account", loginUser.getSysUser().getAccount());
        rspMap.put("phone", loginUser.getSysUser().getAccount());
        rspMap.put("roles", loginUser.getRoles());
        rspMap.put("head_img_url", loginUser.getHeadImgUrl());
        rspMap.put("sex", loginUser.getSysUser().getSex());
        return rspMap;
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }

    @Override
    public Map<String, Object> createTokenForMember(LoginUser loginUser) {
        String token = StrUtil.isNotBlank(loginUser.getToken()) ? loginUser.getToken() : UUID.randomUUID().toString();
        Long userId = loginUser.getUserid();
        String userName = loginUser.getUsername();
        loginUser.setToken(token);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>(4);
        claimsMap.put(SecurityConstants.USER_KEY, token);
        // 买家用户 token
        claimsMap.put(SecurityConstants.USER_TYPE, UserConstants.USER_STUDENT);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        rspMap.put("member_id", userId);
        rspMap.put("user_name", userName);
        rspMap.put("user_account", loginUser.getAccount());
        rspMap.put("head_img_url", loginUser.getHeadImgUrl());
        rspMap.put("sex", loginUser.getSysUser().getSex());
        return rspMap;
    }
}
