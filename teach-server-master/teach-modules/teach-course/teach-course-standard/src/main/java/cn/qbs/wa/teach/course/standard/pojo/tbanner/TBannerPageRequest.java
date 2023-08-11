package cn.qbs.wa.teach.course.standard.pojo.tbanner;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;
import lombok.EqualsAndHashCode;

/**
 * Banner图片(TBanner)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TBannerPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "数据板块(course-课程 activity-活动)")
    private String section;

    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

}

