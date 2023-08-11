package cn.qbs.wa.teach.organization.pojo.organizationrolemenu;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【角色菜单关联关系】(OrganizationRoleMenu)更新【角色菜单关联关系】参数
 *
 * @author makejava
 * @since 2021-11-12 08:58:35
 */
@Data
public class OrganizationRoleMenuUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

    @ApiModelProperty(value = "【菜单ID】")
    private Long menuId;

}

