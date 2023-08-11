package cn.qbs.wa.teach.course.standard.pojo.tbanner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.standard.entity.TBanner;
import lombok.EqualsAndHashCode;

/**
 * Banner图片(TBanner)分页查询Banner图片响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
@Data
public class TBannerResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "数据板块(course-课程 activity-活动)")
    private String section;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

}

