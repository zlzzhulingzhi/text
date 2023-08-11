package cn.qbs.wa.teach.course.standard.pojo.tbanner;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * Banner图片(TBanner)创建Banner图片参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
@Data
public class TBannerAddRequest {
    
    @ApiModelProperty(value = "数据板块(course-课程 activity-活动)")
    private String section;
    
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;
    
    @ApiModelProperty(value = "排序号")
    private Integer sort;
    
    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

}

