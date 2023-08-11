package cn.qbs.wa.teach.organization.pojo.organizationrole;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【机构角色】(OrganizationRole)分页查询参数
 *
 * @author makejava
 * @since 2021-11-10 08:42:58
 */
@Data
public class OrganizationRolePageRequest extends BasePageRequest {

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

}

