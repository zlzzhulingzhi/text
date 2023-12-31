package cn.qbs.wa.teach.common.post.core;

import java.awt.*;

/**
 * 颜色转换工具类
 * @author qbhy
 */
public class ColorTools {
    /**
     * Color 转字符串
     *
     * @param color
     *
     * @return
     */
    public static String Color2String(Color color) {
        String R = Integer.toHexString(color.getRed());
        R = R.length() < 2 ? ('0' + R) : R;
        String B = Integer.toHexString(color.getBlue());
        B = B.length() < 2 ? ('0' + B) : B;
        String G = Integer.toHexString(color.getGreen());
        G = G.length() < 2 ? ('0' + G) : G;
        return '#' + R + B + G;
    }

    public static Color String2Color(String str, double opacity) {
        int i = Integer.parseInt(str.substring(1), 16);
        return new Color(i);
    }

    /**
     * 字符串转Color
     *
     * @param str
     *
     * @return
     */
    public static Color String2Color(String str) {
        return ColorTools.String2Color(str, 1.0);
    }
}
