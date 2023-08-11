package cn.qbs.wa.teach.common.core.utils;

/**
 * @Author zcm
 * @Date 2021/10/22 11:40
 * @Version 1.0
 */
public class DateFormatUtils {

    /**
     * 秒数转化为时分秒字符串
     *
     * @param seconds
     * @return String
     */
    public static String secondsFormat(long seconds) {
        int daySeconds = 86400;
        long days = seconds / daySeconds;//转换天数
        seconds = seconds % daySeconds;//剩余秒数

        int hourSeconds = 3600;
        long hours = seconds / hourSeconds;//转换小时数
        seconds = seconds % hourSeconds;//剩余秒数

        int minuteSeconds = 60;
        long minutes = seconds / minuteSeconds;//转换分钟
        seconds = seconds % minuteSeconds;//剩余秒数

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分");
        }

//        sb.append(String.format("%02d", seconds)).append("秒");
        sb.append(seconds).append("秒");

        return sb.toString();
    }

}
