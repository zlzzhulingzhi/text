package cn.qbs.wa.union.admin.pojo.uniorganization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.awt.font.LayoutPath;
import java.util.List;

/**
 * 统一机构(UniOrganization)分页查询统一机构响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@Data
public class UniOrganizationListRequest {

    @ApiModelProperty(value = "机构id数组")
    private List<Long> ids;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;
}

