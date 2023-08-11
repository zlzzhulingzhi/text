package cn.qbs.wa.teach.course.standard.util;


import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import cn.qbs.wa.teach.course.standard.enums.ImageSourceType;
import cn.qbs.wa.teach.course.standard.pojo.report.CourseReportDto;
import cn.qbs.wa.teach.course.standard.pojo.report.ImageResourceDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author cbd
 * @desc
 * @date 2021-02-27
 **/
public class ReportGenerateUtil {

    private static ConcurrentHashMap<String,byte[]> inputStreamMap = new ConcurrentHashMap();

    public static BufferedImage createCourseReport(CourseReportDto dto) throws Exception {

        BufferedImage img = new BufferedImage(750, 1124, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        img = graphics.getDeviceConfiguration().createCompatibleImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);
        graphics = img.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, img.getWidth(), img.getHeight());
        addPictureImage(graphics,750,562,0, 0,dto.getCover());
        addPictureImage(graphics,137,138,585, 602,dto.getQrCode());
        addPictureImage(graphics,64,64,32, 1010,new Color(236, 255, 246, 255),dto.getAvatar());
        addPictureImage(graphics,176,48,547, 1017,dto.getLogo());
        addLineImage(graphics,686,2,new Color(204, 204, 204, 255),null,true, 32, 964);

        Font font;
        Color color;
        Color backgroundColor;
        font = FontUtil.getFont(FontUtil.PINGFANG_BOLD_FONT, 48.0f);
        color = new Color(51, 51, 51, 255);
        addTextImage(graphics,font,color,32, 602, dto.getCourseName(),9,2,0,false,false);

        font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 32.0f);
        color = new Color(239, 80, 94, 255);
        //addTextImage(graphics,font,color,32, 812, CourseReportDto.CNY_SIGN,null,null,0,false,false);

        //判断是否优惠活动
        if(StringUtils.isNotEmpty(dto.getDiscountTypeTag())){
            font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 48.0f);
            addTextImage(graphics,font,color,66, 793, dto.getDiscountPrice(),null,null,0,false,false);
            int padding = GraphicsUtil.getTextRect(font,dto.getDiscountPrice()).getWidth() + 20;

            font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 25.0f);
            backgroundColor = new Color(254, 195, 63, 255);
            addTextImage(graphics,font,color,backgroundColor,66+padding, 804, dto.getDiscountTypeTag(),null,null,21,false,false);

            font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 24.0f);
            color = new Color(170, 170, 170, 255);
            //addTextImage(graphics,font,color,38, 856, CourseReportDto.CNY_SIGN + dto.getGoodsPrice(),null,null,0,false,true);
        }else{
            font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 48.0f);
            //addTextImage(graphics,font,color,66, 793, dto.getGoodsPrice(),null,null,0,false,false);
        }

        font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 24.0f);
        color = new Color(153, 153, 153, 255);
        addTextImage(graphics,font,color,582, 808, CourseReportDto.QR_CODE_TIPS,null,null,0,false,false);

        font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 32.0f);
        color = new Color(51, 51, 51, 255);
//        addTextImage(graphics,font,color,108, 1017, CourseReportDto.SHARE_TIPS,null,null,0,false,false);
        BufferedImage nickImage = addTextImage(graphics, font, color, 108, 1017, "[" + dto.getNickName() + "]", null, null, 0, false, false);
        if(dto.getShareLogo()!=null){
            font = FontUtil.getFont(FontUtil.PINGFANG_BOLD_FONT, 32.0f);
            addPictureImage(graphics,122,44,nickImage.getWidth()+118, 1017,dto.getShareLogo());
        }

        graphics.dispose();

        return img;
    }

    /*public static BufferedImage createMicroReport(MicroReportDto dto) throws Exception {
        if(dto.isShared()){
            return createMicroShareReport(dto);
        }else{
            return createMicroDefaultReport(dto);
        }
    }

    public static BufferedImage createMicroDefaultReport(MicroReportDto dto) throws Exception {

        BufferedImage img = new BufferedImage(750, 1124, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        img = graphics.getDeviceConfiguration().createCompatibleImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);
        graphics = img.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, img.getWidth(), img.getHeight());

        addPictureImage(graphics,750,562,0, 0,dto.getCover());
        addPictureImage(graphics,137,138,585, 602,dto.getQrCode().getResource());
        addPictureImage(graphics,64,64,32, 1010,new Color(236, 255, 246, 255),dto.getAvatar().getResource());
        addPictureImage(graphics,176,48,547, 1017,dto.getLogo().getResource());
        addLineImage(graphics,686,2,new Color(204, 204, 204, 255),null,true, 32, 964);

        Font font;
        Color color;

        font = FontUtil.getFont(FontUtil.PINGFANG_BOLD_FONT, 48.0f);
        color = new Color(51, 51, 51, 255);
        addTextImage(graphics,font,color,32, 602, dto.getMicroName(),9,2,0,false,false);

        font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 28.0f);
        color = new Color(102, 102, 102, 255);
        addTextImage(graphics,font,color,32, 812, CourseReportDto.TIME_ZONE,null,null,0,false,false);

        font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 24.0f);
        color = new Color(153, 153, 153, 255);
        addTextImage(graphics,font,color,582, 808, CourseReportDto.QR_CODE_TIPS,null,null,0,false,false);

        font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 32.0f);
        color = new Color(51, 51, 51, 255);
//        addTextImage(graphics,font,color,108, 1017, CourseReportDto.SHARE_TIPS,null,null,0,false,false);

        font = FontUtil.getFont(FontUtil.PINGFANG_BOLD_FONT, 32.0f);
        BufferedImage nickImage = addTextImage(graphics, font, color, 108, 1017, "[" + dto.getNickName().getText() + "]", null, null, 0, false, false);

        if(dto.getShareLogo()!=null){
            addPictureImage(graphics,122,44,nickImage.getWidth()+118, 1017,dto.getShareLogo().getResource());
        }


        graphics.dispose();

        return img;
    }

    public static BufferedImage createMicroShareReport(MicroReportDto dto) throws Exception {

        BufferedImage img = new BufferedImage(480, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = img.createGraphics();
        img = graphics.getDeviceConfiguration().createCompatibleImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);
        graphics = img.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, img.getWidth(), img.getHeight());

        addPictureImage(graphics,480,720,0, 0,dto.getCover());

        ReportUnitDto.ImageUnitDto avatar = dto.getAvatar();
        addPictureImage(graphics,50,50,avatar.getX(), avatar.getY(),new Color(236, 255, 246, 255),dto.getAvatar().getResource());

        ReportUnitDto.ImageUnitDto qrCode = dto.getQrCode();
        addPictureImage(graphics,100,100,qrCode.getX(), qrCode.getY(),qrCode.getResource());

        ReportUnitDto.TextUnitDto nickName = dto.getNickName();
        Font font = FontUtil.getFont(FontUtil.PINGFANG_FONT, 24.0f);
        Color color = new Color(51, 51, 51, 255);
//        addTextImage(graphics,font,color,nickName.getX(), nickName.getY(), CourseReportDto.SHARE_TIPS,null,null,0,false,false);

        int padding = GraphicsUtil.getTextRect(font,CourseReportDto.SHARE_TIPS).getWidth() + 10;
        font = FontUtil.getFont(FontUtil.PINGFANG_BOLD_FONT, 24.0f);
        BufferedImage nickImage = addTextImage(graphics, font, color, nickName.getX() + padding, nickName.getY(), "[" + nickName.getText() + "]", null, null, 0, false, false);

        if(dto.getShareLogo()!=null){
            addPictureImage(graphics,122,44,nickImage.getWidth()+nickName.getX()+10, 1017,dto.getShareLogo().getResource());
        }

        graphics.dispose();

        return img;
    }*/

    private static BufferedImage fetchImageResource(ImageResourceDto res) throws IOException {

        BufferedImage image = null;
        if(ImageSourceType.URL.equals(res.getType())){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //System.out.println("https:" + res.getUrl());
            if(res.getUrl().contains("http")){
                HttpUtil.download( res.getUrl(),out,true);
            }else {
                HttpUtil.download("https:" + res.getUrl(),out,true);
            }
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            image = ImageIO.read(in);
        }else if(ImageSourceType.FILE.equals(res.getType())){
            image = ImageIO.read(new File(res.getUrl()));
        }else if(ImageSourceType.INPUT.equals(res.getType())){
            byte[] bs = inputStreamMap.get(res.getUrl());
            if(bs == null){
                ClassPathResource classPathResource = new ClassPathResource(res.getUrl());
                try(InputStream inputStream = classPathResource.getInputStream()) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IoUtil.copy(inputStream,out);
                    bs = out.toByteArray();
                    inputStreamMap.put(res.getUrl(),bs);
                }
            }

            ByteArrayInputStream in = new ByteArrayInputStream(bs);
            image = ImageIO.read(in);
        }

        return image;
    }

    /**
     * 添加图片
     * @param graphics
     * @param width 宽
     * @param height 高
     * @param x x坐标
     * @param y y坐标
     * @param res 图片资源
     * @throws Exception
     */
    public static void addPictureImage(Graphics2D graphics, int width, int height, int x, int y, ImageResourceDto res) throws Exception {

        BufferedImage image = fetchImageResource(res);
        graphics.drawImage(image.getScaledInstance(width, height, Image.SCALE_DEFAULT), x, y, null);
    }

    /**
     * 添加圆形裁剪图片
     * @param graphics
     * @param width
     * @param height
     * @param x
     * @param y
     * @param backgroundColor 裁剪背景
     * @param res
     * @throws Exception
     */
    public static void addPictureImage(Graphics2D graphics, int width, int height, int x, int y,Color backgroundColor, ImageResourceDto res) throws Exception {
        System.out.println(res);
        BufferedImage image = fetchImageResource(res);

        graphics.drawImage(GraphicsUtil.cutRoundImage(image,backgroundColor).getScaledInstance(width, height, Image.SCALE_DEFAULT), x, y, null);
    }

    /**
     * 添加文本
     * @param graphics
     * @param font   字体
     * @param color 颜色
     * @param x x坐标
     * @param y y坐标
     * @param content  文本
     * @param limitChar 单行限制字符
     * @param limitRow 文本展示行数
     * @param padding 文本行前后间距
     * @param underline 是否下划线
     * @param strike 是否删除线
     */
    public static BufferedImage addTextImage(Graphics2D graphics, Font font, Color color,int x,int y, String content,Integer limitChar,Integer limitRow,int padding,boolean underline,boolean strike)  {

        BufferedImage image = GraphicsUtil.createTextImage(content,font,color,null,limitChar,limitRow,padding,underline,strike);
        graphics.drawImage(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT), x, y, null);
        return image;
    }

    /**
     * 添加背景块文本
     * @param graphics
     * @param font   字体
     * @param color 颜色
     * @param backgroundColor 背景颜色
     * @param x x坐标
     * @param y y坐标
     * @param content  文本
     * @param limitChar 单行限制字符
     * @param limitRow 文本展示行数
     * @param padding 文本行前后间距
     * @param underline 是否下划线
     * @param strike 是否删除线
     */
    public static void addTextImage(Graphics2D graphics, Font font, Color color, Color backgroundColor,int x,int y, String content,Integer limitChar,Integer limitRow,int padding,boolean underline,boolean strike)  {

        BufferedImage image = GraphicsUtil.createArchTextImage(content,font,color,backgroundColor,limitChar,limitRow,padding,underline,strike);
        graphics.drawImage(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT), x, y, null);
    }

    /**
     * 添加横线
     * @param graphics
     * @param width  线长
     * @param boldSize 线粗
     * @param color 线颜色
     * @param backgroundColor 线背景颜色
     * @param stroke 是否虚线
     * @param x x坐标
     * @param y y坐标
     */
    public static void addLineImage(Graphics2D graphics,int width,int boldSize,Color color,Color backgroundColor,boolean stroke,int x,int y){

        BufferedImage image = GraphicsUtil.createLineImage(width,boldSize,color,backgroundColor,stroke);
        graphics.drawImage(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_DEFAULT), x, y, null);
    }
}

