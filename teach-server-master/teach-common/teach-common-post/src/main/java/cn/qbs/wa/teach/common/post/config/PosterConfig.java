package cn.qbs.wa.teach.common.post.config;


import cn.qbs.wa.teach.common.post.core.ResourceUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConfigurationProperties(prefix = "poster")
@Data
@Slf4j
public class PosterConfig {

    // 程序当前运行目录
    private static final String current = System.getProperty("user.dir");

    /**
     * 下载路径
     */
    private String downloadPath = current + "/downloads";

    /**
     * 模板路径
     */
    private String templatesPath = current + "/templates";

    /**
     * 模板路径
     */
    private String fontsPath = "fonts/";

    /**
     * 上传方式
     */
    private String Uploader = "smmsUploader";

    /**
     * 获取文件下载目录
     *
     * @return String
     */
    public String getDownloadPath() {
        return withTail(downloadPath);
    }

    /**
     * 获取文件下载后的地址
     *
     * @param url
     * @return String
     */
    public String getDownloadPath(String url) {
        return getDownloadPath() + url2fileName(url);
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getTemplatesPath() {
        return withTail(templatesPath);
    }

    public String getTemplatesPath(String imageName) {
        return getTemplatesPath() + imageName;
    }

    public String getFontsPath() {
        return withTail(fontsPath);
    }

    public String getFontsPath(String font) {
        return getFontsPath() + font;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public void setFontsPath(String fontsPath) {
        this.fontsPath = fontsPath;
    }

    public static String withTail(String path) {
        return path + (path.endsWith("/") ? "" : "/");
    }

    /**
     * 从模板中获取图片
     *
     * @param imageName
     * @return imageFile
     * @throws IOException
     */
    public BufferedImage getTemplateImage(String imageName) throws IOException {

        // 从用户自定义的目录中找
        File imageFile = new File(getTemplatesPath(imageName));
        if (imageFile.exists()) {
            return ImageIO.read(imageFile);
        }

        // 找不到的话从默认模板中找
        imageFile = new File(ResourceUtils.getResourcePath("templates/" + imageName));
        if (imageFile.exists()) {
            return ImageIO.read(imageFile);
        }

        // 实在找不到就抛异常
        throw new IOException("file not found!");
    }

    /**
     * 从字体库中获取字体
     *
     * @param font
     * @return File
     * @throws IOException
     */
    public InputStream getFontFile(String font) throws IOException {
        log.info("字体库获取字体{}", font);
        String fontUrl = (fontsPath + font);
        return new FileInputStream(fontUrl);
        // 实在找不到就抛异常
    }

    /**
     * 获取下载过的文件
     *
     * @param url
     * @return File
     */
    public File getDownloadedFile(String url) {
        File imageFile = new File(getDownloadPath(url));

        if (imageFile.exists()) {
            return imageFile;
        }

        return null;
    }

    public String url2fileName(String url) {
        return DigestUtils.md5DigestAsHex(url.getBytes()) + ".png";
    }


}
