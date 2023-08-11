package cn.qbs.wa.teach.exam.common.util;

import java.util.Date;

/**
 * @Author zcm
 * @Date 2021/12/9 19:08
 * @Version 1.0
 */
public class DateUtil {

    public static String nowDateStr() {
        return cn.hutool.core.date.DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

}
