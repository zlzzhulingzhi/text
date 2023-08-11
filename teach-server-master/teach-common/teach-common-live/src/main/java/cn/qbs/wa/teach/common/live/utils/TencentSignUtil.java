package cn.qbs.wa.teach.common.live.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/6 11:01
 */
public class TencentSignUtil {



    /**
     * 　　* @description: 点播签名 key为防盗key
     * 　　* @author vieux
     * 　　* @date 2022/1/6 11:18
     *
     */
    public static String getDianBoSign(Integer appId, String fileId, String key) {
        Long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        Long psignExpire = LocalDateTime.now().plusHours(8).toEpochSecond(ZoneOffset.of("+8"));
        Integer AppId = appId;
        String FileId = fileId;
        String Key = key;
        HashMap<String, String> urlAccessInfo = new HashMap<String, String>();
        try {
            Algorithm algorithm = Algorithm.HMAC256(Key);
            String token = JWT.create().withClaim("appId", AppId).withClaim("fileId", FileId)
                    .withClaim("currentTimeStamp", currentTime).withClaim("expireTimeStamp", psignExpire)
                    .withClaim("urlAccessInfo", urlAccessInfo).sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }




}
