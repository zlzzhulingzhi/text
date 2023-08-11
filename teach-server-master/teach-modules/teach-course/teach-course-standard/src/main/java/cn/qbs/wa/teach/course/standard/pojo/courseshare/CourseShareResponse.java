package cn.qbs.wa.teach.course.standard.pojo.courseshare;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseShareResponse {

    @ApiModelProperty(value = "二维码图片")
    private String qrcodeUrl;

    @ApiModelProperty(value = "海报图片")
    private String reportUrl;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String phone;
}
