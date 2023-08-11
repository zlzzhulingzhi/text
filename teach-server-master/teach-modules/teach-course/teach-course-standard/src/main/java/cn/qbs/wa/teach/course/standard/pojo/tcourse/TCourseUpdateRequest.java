package cn.qbs.wa.teach.course.standard.pojo.tcourse;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 课程-报名分享(TCourse)更新课程-报名分享参数
 *
 * @author makejava
 * @since 2022-10-28 16:39:15
 */
@Data
public class TCourseUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "二维码图片")
    private String qrcodeUrl;

    @ApiModelProperty(value = "海报图片")
    private String reportUrl;

}

