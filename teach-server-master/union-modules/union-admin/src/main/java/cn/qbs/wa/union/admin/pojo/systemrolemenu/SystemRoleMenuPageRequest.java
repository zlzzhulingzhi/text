package cn.qbs.wa.union.admin.pojo.systemrolemenu;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【角色菜单关联关系】(SystemRoleMenu)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Data
public class SystemRoleMenuPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Long orgId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

    @ApiModelProperty(value = "【菜单ID】")
    private Long menuId;

}

