package cn.qbs.wa.teach.organization.pojo.organizationrolemenu;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【角色菜单关联关系】(OrganizationRoleMenu)分页查询参数
 *
 * @author makejava
 * @since 2021-11-12 08:58:32
 */
@Data
public class OrganizationRoleMenuPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

    @ApiModelProperty(value = "【菜单ID】")
    private Long menuId;

}

