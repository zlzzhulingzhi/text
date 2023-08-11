package cn.qbs.wa.teach.basic.controller;


import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.basic.pojo.role.RoleAddRequest;
import cn.qbs.wa.teach.basic.pojo.role.RolePageRequest;
import cn.qbs.wa.teach.basic.pojo.role.RolePermissionResponse;
import cn.qbs.wa.teach.basic.pojo.role.RoleUpdateRequest;
import cn.qbs.wa.teach.basic.service.RoleService;
import cn.qbs.wa.teach.basic.service.UserRoleService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【系统角色】(Role)表控制层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
public class RoleController {

    /**
     * 服务对象
     */
    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param role 查询实体
     * @return 所有数据
     */
    @PostMapping("page")
    // @RequiresPermissions("role:page")
    @Log(title = "分页查询【系统角色】", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R page(@RequestBody RolePageRequest request) {
        return R.ok(this.roleService.pageRole(request));
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("list")
    // @RequiresPermissions("role:page")
    @Log(title = "查询所有【系统角色】", businessType = BusinessType.OTHER)
    @ApiOperation("列表查询")
    public R list() {
        List<Role> list = this.roleService.list(new LambdaQueryWrapper<Role>().eq(Role::getEnabled, Constants.DB_TRUE));
        // 不能选择比当前操作人员角色高的角色
        userRoleService.filterInvalid(list);
        return R.ok(list);
    }



    @PostMapping("admin-role-permission")
    // @RequiresPermissions("role:page")
    @Log(title = "获取平台角色权限菜单栏", businessType = BusinessType.OTHER)
    @ApiOperation("获取平台角色权限菜单栏-只包含平台")
    public R getRoleAdminPermission() {
        RolePermissionResponse response = this.roleService.getRoleAdminPermission();
        return R.ok(response);
    }

    @PostMapping("org-role-permission")
    // @RequiresPermissions("role:page")
    @Log(title = "获取平台角色权限菜单栏", businessType = BusinessType.OTHER)
    @ApiOperation("获取平台角色权限菜单栏-不包含平台")
    public R getRoleOrgPermission() {
        RolePermissionResponse response = this.roleService.getRoleOrgPermission();
        return R.ok(response);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("detail")
    // @RequiresPermissions("role:details")
    @Log(title = "【系统角色】详情", businessType = BusinessType.OTHER)
    @ApiOperation("角色详情")
    public R details(@RequestBody IdRequest request) {
        return R.ok(this.roleService.getRole(request.getId()));
    }



    /**
     * 平台管理修改【系统角色】
     *
     * @param role 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    // @RequiresPermissions("role:update")
    @Log(title = "平台管理修改角色", businessType = BusinessType.UPDATE)
    @ApiOperation("平台管理修改统角色")
    public R updateAdminRole(@RequestBody RoleUpdateRequest request) {
        this.roleService.updateAdminRole(request);
        return R.ok();
    }


    /**
     * 平台管理新增角色
     *
     * @param role 实体对象
     * @return 新增结果
     */
    @PostMapping("add")
    // @RequiresPermissions("role:add")
    @Log(title = "平台管理新增角色", businessType = BusinessType.INSERT)
    @ApiOperation("平台管理新增角色")
    public R addAdminRole(@RequestBody RoleAddRequest request) {
        this.roleService.addAdminRole(request);
        return R.ok();
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @PostMapping("delete")
    //@RequiresPermissions("role:delete")
    @Log(title = "删除【系统角色】", businessType = BusinessType.DELETE)
    @ApiOperation("角色删除")
    public R delete(@RequestBody IdListRequest request) {
        return R.ok(this.roleService.removeByIds(request.getIdList()));
    }

    /**
     * 批量启用/禁用
     *
     *
     *
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.roleService.batchEnabled(request.getFlag(),request.getIdList());
        return R.ok();
    }

}

