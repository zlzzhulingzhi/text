package cn.qbs.wa.train.logistics.pojo.organizationmenu;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【角色菜单关联关系】(OrganizationMenu)更新【角色菜单关联关系】参数
 *
 * @author makejava
 * @since 2021-11-09 20:26:25
 */
@Data
public class OrganizationMenuUpdateRequest {

    @ApiModelProperty(value = "")
    private Integer id;

    @ApiModelProperty(value = "【应用模块ID】")
    private Integer orgId;

    @ApiModelProperty(value = "【菜单ID】")
    private Integer menuId;

}

