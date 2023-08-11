package cn.qbs.wa.teach.out.union.admin.api.DTO;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 统一机构(UniOrganization)分页查询统一机构响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniOrgSearchDTO extends BasePageSearchComDTO {

    @ApiModelProperty(value = "机构id数组")
    private List<Long> ids;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;
}

