package cn.qbs.wa.train.logistics.pojo.organizationmenu;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【角色菜单关联关系】(OrganizationMenu)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:26:20
 */
@Data
public class OrganizationMenuPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Integer orgId;

    @ApiModelProperty(value = "【菜单ID】")
    private Integer menuId;

}

