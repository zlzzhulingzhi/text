package cn.qbs.wa.union.admin.pojo.systemuser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)【角色菜单关联关系】详情
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
@Data
public class SystemUserRoleMenuDataResponse {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【上级菜单ID】")
    private Long parentId;

    @ApiModelProperty(value = "【菜单名称】")
    private String name;

    @ApiModelProperty(value = "【菜单URI】")
    private String uri;

    @ApiModelProperty(value = "【菜单说明】")
    private String remark;

    @ApiModelProperty(value = "【菜单图标URL】")
    private String iconUrl;

    @ApiModelProperty(value = "【菜单类别】 预留")
    private String category;

    @ApiModelProperty(value = "【菜单排序】")
    private Integer sort;

    @ApiModelProperty("角色名称")
    String roleName;

    @ApiModelProperty("权限")
    String permission;


}

