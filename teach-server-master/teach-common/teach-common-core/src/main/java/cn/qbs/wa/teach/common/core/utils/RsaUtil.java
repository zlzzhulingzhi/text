package cn.qbs.wa.teach.common.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.qbs.wa.teach.common.core.config.RSAConfig;

import java.util.Base64;

public class RsaUtil {

    public static String decodePassWord(String password) {
        if (StrUtil.isNotBlank(password)) {
            RSA rsa = new RSA(RSAConfig.privateKey, RSAConfig.publicKey);
            byte[] decode = Base64.getDecoder().decode(password);
            byte[] decrypt = rsa.decrypt(decode, KeyType.PrivateKey);
            return new String(decrypt);
        }
        return null;
    }

}