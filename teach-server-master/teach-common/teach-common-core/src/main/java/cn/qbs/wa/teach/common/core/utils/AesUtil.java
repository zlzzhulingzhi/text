package cn.qbs.wa.teach.common.core.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/15 16:43
 */
@Slf4j
public class AesUtil {

    static String secretKey = "bK9kMSVkvwgeLZyhhNcjtQ==";


    public static String encode(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }

        //构建
        byte[] key = Base64.getMimeDecoder().decode(secretKey);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        //加密为16进制表示
        return aes.encryptHex(content);
    }

    public static String decode(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }

        try {
            byte[] key = Base64.getMimeDecoder().decode(secretKey);
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
            return aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            return content;
        }
    }


}
