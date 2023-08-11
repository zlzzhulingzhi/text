package cn.qbs.wa.teach.organization.pojo.organizationmenu;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 【角色菜单关联关系】(OrganizationMenu)创建【角色菜单关联关系】参数
 *
 * @author makejava
 * @since 2021-11-09 20:26:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationMenuVO {

    @ApiModelProperty(value = "【菜单ID】")
    private Long id;

}

