package cn.qbs.wa.train.screen.pojo.screen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author makejava
 * @since 2022-10-09 17:04:41
 */
@Data
public class DataOverviewVO implements Serializable {

    @ApiModelProperty(value = "字段代号")
    private String fieldCode;

    @ApiModelProperty(value = "显示名称")
    private String displayName;

    @ApiModelProperty(value = "显示值")
    private String displayValue;

    @ApiModelProperty(value = "显示图标")
    private String displayIcon;
}
