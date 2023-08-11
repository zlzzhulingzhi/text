package cn.qbs.wa.teach.course.standard.pojo.report;

import lombok.Data;

@Data
public class CourseReportDto {

    public static final String QR_CODE_TIPS = "长按扫描识别";
    public static final String SHARE_TIPS = "来自好友";
    public static final String CNY_SIGN = "￥";
    public static final String TIME_ZONE = "活动时间：";

    private ImageResourceDto cover;
    private ImageResourceDto avatar;
    private ImageResourceDto qrCode;
    private ImageResourceDto logo;
    private ImageResourceDto shareLogo;

    private String nickName;
    private String courseName;
    private String goodsPrice;
    private String discountTypeTag;
    private String discountPrice;
}