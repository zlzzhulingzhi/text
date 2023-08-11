package cn.qbs.wa.union.admin.pojo.systemuser;

import cn.qbs.wa.union.admin.pojo.systemmenu.SystemMenuTreeResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)【角色菜单关联关系】详情
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
@Data
public class SystemUserRoleMenuMineResponse {


    @ApiModelProperty("角色名称")
    List<String> roles;

    @ApiModelProperty("权限列表")
    List<String> permissions;

    @ApiModelProperty("菜单列表")
    List<SystemMenuTreeResponse> menuList;



}

