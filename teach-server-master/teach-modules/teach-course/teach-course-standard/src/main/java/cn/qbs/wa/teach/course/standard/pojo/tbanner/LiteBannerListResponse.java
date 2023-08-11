package cn.qbs.wa.teach.course.standard.pojo.tbanner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiteBannerListResponse {

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;
}
