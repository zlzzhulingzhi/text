package cn.qbs.wa.teach.organization.pojo.organizationrolemenu;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【角色菜单关联关系】(OrganizationRoleMenu)创建【角色菜单关联关系】参数
 *
 * @author makejava
 * @since 2021-11-12 08:58:34
 */
@Data
public class OrganizationRoleMenuAddRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

    @ApiModelProperty(value = "【菜单ID】")
    private Long menuId;

}

