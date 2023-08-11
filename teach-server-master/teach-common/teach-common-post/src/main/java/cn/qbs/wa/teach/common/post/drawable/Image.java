package cn.qbs.wa.teach.common.post.drawable;

import cn.qbs.wa.teach.common.post.core.Drawable;
import cn.qbs.wa.teach.common.post.core.ResourceUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
public class Image extends Drawable {

    @Override
    public void draw(Graphics2D gd, int posterWidth, int posterHeight) throws IOException {
        BufferedImage image;

        if (qrCode) {
            // 是二维码图片的话创建一个二维码
            try {
                image = createQrCode(url, width, height, qrCodeMargin);
            } catch (WriterException e) {
                throw new IOException("生成二维码失败", e);
            }
        } else {
            // 获取图片
            image = ResourceUtils.getImage(url);
        }

        // 如果宽高不合适，先缩放
        if (image.getWidth() != width || image.getHeight() != height) {
            image = resize(image, width, height);
        }

        // 处理圆角，todo: 实现的不完美，待优化
        if (borderRadius > 0) {
            image = ResourceUtils.setRadius(image, borderRadius * 4, 0, -1);
        }

        // 画图
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gd.drawImage(image, x, y, width, height, (img, infoFlags, x, y, width, height) -> false);
    }

    /**
     * 缩放图片
     *
     * @param image  需要缩放的图片
     * @param width  宽
     * @param height 高
     * @return BufferedImage
     */
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        java.awt.Image img = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);

        BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = newBufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return newBufferedImage;
    }

    /**
     * z index 值
     */
    private int index = 1;

    @Override
    public int getZIndex() {
        return index;
    }

    /**
     * x 值
     */
    @NotNull(message = "图片X坐标不能为空")
    @ApiModelProperty(value = "图片X坐标")
    private Integer x = 0;

    /**
     * y 值
     */
    @NotNull(message = "图片Y坐标不能为空")
    @ApiModelProperty(value = "图片Y坐标")
    private Integer y = 0;

    /**
     * 宽度
     */
    @NotNull(message = "图片宽度不能为空")
    @ApiModelProperty(value = "图片宽度")
    private Integer width;

    /**
     * 高度
     */
    @NotNull(message = "图片高度不能为空")
    @ApiModelProperty(value = "图片高度")
    private Integer height;

    /**
     * border radius
     */
    @Min(value = 0, message = "图片圆角不能小于0")
    private Integer borderRadius = 0;

    /**
     * url
     */
    @NotEmpty(message = "图片url不能为空")
    @ApiModelProperty(value = "图片路径")
    private String url;

    /**
     * 是否是二维码图片
     */
    private boolean qrCode = false;

    /**
     * 二维码边距
     */
    @Min(value = 0, message = "二维码边距最小为0")
    private int qrCodeMargin = 2;

    /**
     * 创建二维码
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @param margin  二维码边距
     * @return BufferedImage 返回图片
     * @throws WriterException 异常
     */
    public static BufferedImage createQrCode(String content, int width, int height, int margin) throws WriterException {

        Map<EncodeHintType, Comparable> hints = new HashMap<>();

        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 字符串编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 纠错等级
        hints.put(EncodeHintType.MARGIN, margin); // 图片边距
        QRCodeWriter writer = new QRCodeWriter();

        return MatrixToImageWriter.toBufferedImage(writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints));
    }

    /**
     * 创建二维码
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @return BufferedImage 返回图片
     * @throws WriterException 异常
     */
    public static BufferedImage createQrCode(String content, int width, int height) throws WriterException {
        return createQrCode(content, width, height, 2);
    }

}
