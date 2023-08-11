package cn.qbs.wa.union.auth.pojo.systemrolemenu;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)创建【角色菜单关联关系】参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
@Data
public class SystemRoleMenuAddRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

    @ApiModelProperty(value = "【菜单ID】")
    private Long menuId;

}

