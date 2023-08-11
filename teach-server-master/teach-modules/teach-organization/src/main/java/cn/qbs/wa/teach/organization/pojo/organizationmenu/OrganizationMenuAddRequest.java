package cn.qbs.wa.teach.organization.pojo.organizationmenu;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【角色菜单关联关系】(OrganizationMenu)创建【角色菜单关联关系】参数
 *
 * @author makejava
 * @since 2021-11-09 20:26:25
 */
@Data
public class OrganizationMenuAddRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Integer orgId;

    @ApiModelProperty(value = "【菜单ID】")
    private Integer menuId;

}

