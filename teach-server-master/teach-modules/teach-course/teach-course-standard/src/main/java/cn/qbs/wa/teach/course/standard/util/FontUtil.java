package cn.qbs.wa.teach.course.standard.util;

import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class FontUtil {
    /**
     * 默认字体
     */
    public static final int DEFAULT_FONT = 1;
    /**
     * PingFangSC字体
     */
    public static final int PINGFANG_FONT = 2;
    /**
     * PingFangSCBold字体
     */
    public static final int PINGFANG_BOLD_FONT = 3;
    /**
     * 方正兰亭特黑GBK
     */
    public static final int FZLTTH_GBK_FONT = 4;

    private static ConcurrentHashMap<String, Font> fontMap = new ConcurrentHashMap();


    /**
     * 根据字体类型获取字体
     * @param type
     * @param size
     * @return
     */
    public static Font getFont(int type, float size) {

        String key = type+"_"+size;
        if(fontMap.get(key) != null){
            return fontMap.get(key);
        }

        // 字体路径
        String path = "";
        switch (type) {
            case DEFAULT_FONT:
                path = "report/font/simhei.ttf";
                break;
            case PINGFANG_FONT:
                path = "report/font/PingFangSC.ttf";
                break;
            case PINGFANG_BOLD_FONT:
                path = "report/font/PingFangBold.ttf";
                break;
            case FZLTTH_GBK_FONT:
                path = "report/font/fzltthjwgb10.ttf";
                break;
            default:
                path = "report/font/simhei.ttf";
        }

        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(path);
            inputStream =classPathResource.getInputStream();
            Font sPfBoldFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            sPfBoldFont = sPfBoldFont.deriveFont(size);
            fontMap.put(key,sPfBoldFont);
            return sPfBoldFont;
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

