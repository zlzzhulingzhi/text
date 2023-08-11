package cn.qbs.wa.teach.course.standard.util;



import cn.qbs.wa.teach.course.standard.pojo.report.TextRectDto;
import sun.font.FontDesignMetrics;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cbd
 * @desc
 * @date 2021-02-26
 **/
public class GraphicsUtil {

    private static final char SIGN_CHAR = "字".charAt(0);

    /**
     * 创建文本对象
     * @param content 文本
     * @param font 字体
     * @param textColor 字体颜色
     * @param backgroundColor 文本背景颜色
     * @param limitChar 每行限制字符
     * @param limitRow 文本限制行数
     * @param padding 文本前后留空距离
     * @param underline 是否添加下滑线
     * @param strike 是否添加删除线
     * @return
     */
    public static BufferedImage createTextImage(String content, Font font, Color textColor, Color backgroundColor, Integer limitChar, Integer limitRow, int padding, boolean underline, boolean strike) {

        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        TextRectDto textRect = getTextRect(font,content,limitChar,limitRow);//计算图片的宽
        int width = textRect.getWidth();
        int height = textRect.getHeight() * textRect.getRows().size();
        if(padding > 0){
            width += padding*2;
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = bufferedImage.createGraphics();
        bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage(bufferedImage.getWidth(), bufferedImage.getHeight(), Transparency.TRANSLUCENT);
        graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        if(backgroundColor != null){
            graphics.setColor(backgroundColor);
        }else{
            graphics.setColor(new Color(255,255,255,0));
        }
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        graphics.setFont(font);
        graphics.setColor(textColor);
        int index = 1;
        for(String row : textRect.getRows()){
            drawString(graphics,metrics,index,font,row,padding,underline,strike);
            index++;
        }

        graphics.dispose();

        return bufferedImage;
    }

    /**
     * 创建带背景的文本对象
     * @param content
     * @param font
     * @param textColor
     * @param backgroundColor
     * @param limitChar
     * @param limitRow
     * @param padding
     * @param underline
     * @param strike
     * @return
     */
    public static BufferedImage createArchTextImage(String content,Font font,Color textColor,Color backgroundColor,Integer limitChar,Integer limitRow,int padding,boolean underline,boolean strike) {
        // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB
        BufferedImage bi1 = createTextImage(content,font,textColor,backgroundColor,limitChar,limitRow,padding,underline,strike);

        BufferedImage image = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_ARGB);

        //Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());

        Graphics2D g2 = image.createGraphics();
        image = g2.getDeviceConfiguration().createCompatibleImage(bi1.getWidth(), bi1.getHeight(), Transparency.TRANSLUCENT);
        g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fill(new Rectangle(image.getWidth(), image.getHeight()));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        // g2.setClip(arc1);
        // 使用 setRenderingHint 设置抗锯齿
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0,bi1.getWidth(), bi1.getHeight(), bi1.getHeight(), bi1.getHeight());

        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(bi1, 0, 0, bi1.getWidth(), bi1.getHeight(), null);
        g2.dispose();

        return image;
    }

    /**
     * 创建线对象
     * @param width
     * @param boldSize
     * @param color
     * @param backgroundColor
     * @return
     */
    public static BufferedImage createLineImage(int width,int boldSize,Color color,Color backgroundColor,boolean stroke)   {

        BufferedImage bufferedImage = new BufferedImage(width, boldSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        if(backgroundColor != null){
            graphics.setColor(backgroundColor);
        }
        graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics.setColor(color);

        if(stroke){
            float[] dash1 = {2,0,2};
            BasicStroke bs1 = new BasicStroke(boldSize,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL,
                    0.0f,
                    dash1,
                    0f);
            graphics.setStroke(bs1);
        }

        graphics.drawLine(0,   0,   width,   0);

        return bufferedImage;
    }

    /**
     * 图片圆形裁剪
     * @param avatarImage
     * @param boldColor
     * @return
     */
    public static BufferedImage cutRoundImage(BufferedImage avatarImage,Color boldColor)  {

        int width = avatarImage.getWidth();
        // 透明底的图片
        BufferedImage formatAvatarImage = new BufferedImage(width, width, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = formatAvatarImage.createGraphics();
        //把图片切成一个圓
        {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //留一个像素的空白区域，这个很重要，画圆的时候把这个覆盖
            int border = 1;
            //图片是一个圆型
            Ellipse2D.Double shape = new Ellipse2D.Double(border, border, width - border * 2, width - border * 2);
            //需要保留的区域
            graphics.setClip(shape);
            graphics.drawImage(avatarImage, border, border, width - border * 2, width - border * 2, null);
            graphics.dispose();
        }
        //在圆图外面再画一个圆
        {
            //新创建一个graphics，这样画的圆不会有锯齿
            graphics = formatAvatarImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int border = 2;
            //画笔是4.5个像素，BasicStroke的使用可以查看下面的参考文档
            //使画笔时基本会像外延伸一定像素，具体可以自己使用的时候测试
            Stroke s = new BasicStroke(3F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            graphics.setStroke(s);
            graphics.setColor(boldColor);
            graphics.drawOval(border, border, width - border * 2, width - border * 2);
            graphics.dispose();
        }

        return formatAvatarImage;
    }

    private static void drawString(Graphics2D graphics,FontDesignMetrics metrics,int rowLevel,Font font,String content,int padding,boolean underline,boolean strike){
        AttributedString as = new AttributedString(content);
        as.addAttribute(TextAttribute.FONT, font);
        if(underline){
            as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, content.length());
        }

        if(strike){
            as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, content.length());
        }

        graphics.drawString(as.getIterator(), padding, rowLevel * metrics.getAscent());//图片上写文字
    }


    private static TextRectDto getTextRect(Font font, String content, Integer limitChar, Integer limitRow) {

        if(limitChar == null && limitRow == null){
            return getTextRect(font,content);
        }

        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        Integer limit = metrics.charWidth(SIGN_CHAR) * limitChar;
        int height = metrics.getHeight();
        int width = 0;
        int tw = 0;
        List<String> rows = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < content.length(); i++) {
            tw += metrics.charWidth(content.charAt(i));
            if(limit != null && tw >= limit){
                rows.add(content.substring(start,i+1));
                start = i+1;
                if(tw > width){
                    width = tw;
                }
                tw = 0;
            }
            if(rows.size() >= limitRow){
                int lastIndex = rows.size() - 1;
                String last = rows.get(lastIndex);
                last = last.substring(0,last.length()-2)+"...";
                rows.set(lastIndex,last);
                break;
            }
        }

        if(width == 0){
            width = tw;
        }

        if(tw > 0){
            rows.add(content.substring(start));
        }

        return new TextRectDto(width,height,rows);
    }

    public static TextRectDto getTextRect(Font font, String content) {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int height = metrics.getHeight();
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return new TextRectDto(width,height,content);
    }

    public static void write(BufferedImage bufferedImage, String target) throws IOException {
        File file = new File(target);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (OutputStream os = new FileOutputStream(target)) {
            ImageIO.write(bufferedImage, "PNG", os);
        }
    }
}

