package cn.qbs.wa.train.basic.pojo.unit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 单位表(Unit)分页查询单位表响应
 *
 * @author makejava
 * @since 2022-09-29 08:31:22
 */
@Data
public class UnitResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "单位名称")
    private String name;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;
}

