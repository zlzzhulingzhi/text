package cn.qbs.wa.train.basic.pojo.unit;

import cn.qbs.wa.train.basic.entity.Unit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 单位表(Unit)分页查询单位表响应
 *
 * @author makejava
 * @since 2022-09-29 08:31:22
 */
@Data
public class AppUnitResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "单位名称")
    private String name;
}

