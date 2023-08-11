package cn.qbs.wa.teach.course.standard.pojo.tbanner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiteBannerListRequest {
    @ApiModelProperty(value = "数据板块(course-课程 activity-活动)")
    private String section;
}
