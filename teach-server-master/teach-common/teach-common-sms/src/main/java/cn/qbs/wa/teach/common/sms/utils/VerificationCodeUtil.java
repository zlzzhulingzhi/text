package cn.qbs.wa.teach.common.sms.utils;

import java.util.Random;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 9:14
 */
public class VerificationCodeUtil {

    public static String create(Integer length) {
        String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < length; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return flag.toString();


    }

}
