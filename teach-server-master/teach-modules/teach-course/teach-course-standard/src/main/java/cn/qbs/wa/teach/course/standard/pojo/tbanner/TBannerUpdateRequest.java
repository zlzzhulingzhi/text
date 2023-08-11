package cn.qbs.wa.teach.course.standard.pojo.tbanner;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Banner图片(TBanner)更新Banner图片参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
@Data
public class TBannerUpdateRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;
    
    @ApiModelProperty(value = "排序号")
    private Integer sort;
    
    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

}

