package cn.qbs.wa.teach.course.standard.pojo.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxCodeRo {

    @ApiModelProperty(value = "访问URL", example = "http://xxxxx.png")
    private String url;


}
