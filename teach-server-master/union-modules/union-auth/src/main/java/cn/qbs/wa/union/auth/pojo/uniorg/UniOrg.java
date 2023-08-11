package cn.qbs.wa.union.auth.pojo.uniorg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author makejava
 * @since 2022-10-19 09:37:35
 */
@Data
public class UniOrg {

    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

}
