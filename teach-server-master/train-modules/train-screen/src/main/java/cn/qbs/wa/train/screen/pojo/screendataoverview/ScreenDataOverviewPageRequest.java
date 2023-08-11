package cn.qbs.wa.train.screen.pojo.screendataoverview;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 大屏-数据总览(ScreenDataOverview)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 15:37:40
 */
@Data
public class ScreenDataOverviewPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "字段代号")
    private String fieldCode;
    
    @ApiModelProperty(value = "显示名称")
    private String displayName;
    
    @ApiModelProperty(value = "显示值")
    private String displayValue;
    
    @ApiModelProperty(value = "显示图标")
    private String displayIcon;
    
    @ApiModelProperty(value = "排序号")
    private Integer sort;

}

