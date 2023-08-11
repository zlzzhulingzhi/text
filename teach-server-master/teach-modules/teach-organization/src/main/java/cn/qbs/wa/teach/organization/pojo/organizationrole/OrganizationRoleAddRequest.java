package cn.qbs.wa.teach.organization.pojo.organizationrole;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【机构角色】(OrganizationRole)创建【机构角色】参数
 *
 * @author makejava
 * @since 2021-11-10 08:43:00
 */
@Data
public class OrganizationRoleAddRequest {

    @ApiModelProperty(value = "【机构Id】")
    private Long orgId;

    @ApiModelProperty(value = "【角色代码】")
    private String code;

    @ApiModelProperty(value = "【角色名称】")
    private String name;

    @ApiModelProperty(value = "【角色备注】")
    private String remark;

    @ApiModelProperty(value = "【角色排序序号】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

    @ApiModelProperty(value = "菜单id数组")
    List<Long> menuIdList;
}



