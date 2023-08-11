package cn.qbs.wa.train.basic.controller;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.Logical;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.pojo.role.RoleAddRequest;
import cn.qbs.wa.train.basic.pojo.role.RolePageRequest;
import cn.qbs.wa.train.basic.pojo.role.RolePermissionResponse;
import cn.qbs.wa.train.basic.pojo.role.RoleUpdateRequest;
import cn.qbs.wa.train.basic.service.RoleService;
import cn.qbs.wa.train.basic.service.UserRoleService;
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
    @RequiresPermissions("System:Management:Plat:Role")
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
    @RequiresPermissions("System:Management:Plat:Role")
    @ApiOperation("列表查询")
    public R list() {
        List<Role> list = this.roleService.list(new LambdaQueryWrapper<Role>().eq(Role::getEnabled, Constants.DB_TRUE));
        // 不能选择比当前操作人员角色高的角色
        userRoleService.filterInvalid(list);
        return R.ok(list);
    }

    @PostMapping("pageList")
    @ApiOperation("列表分页查询")
    public R pageList(@RequestBody RolePageRequest request) {
        return R.ok(this.roleService.pageListRole(request));
    }
    /*@PostMapping("pageList")
    @ApiOperation("列表分页查询")
    public R<PageRoleResponse> pageList(@RequestBody RolePageRequest request) {
        PageRoleResponse pageRoleResponse=new PageRoleResponse();
        List<Role> roleList=this.roleService.lambdaQuery().eq(Role::getEnabled, Constants.DB_TRUE).list();
        List<Role> roles=roleList.stream().skip(request.getSize()*(request.getCurrent()-1)).limit(request.getSize()).collect(Collectors.toList());
        pageRoleResponse.setRoleList(roles);
        pageRoleResponse.setTotal(roleList.size());
        return R.ok(pageRoleResponse);
    }*/

    @PostMapping("admin-role-permission")
    @RequiresPermissions("System:Management:Plat:Role")
    @Log(title = "获取平台角色权限菜单栏", businessType = BusinessType.OTHER)
    @ApiOperation("获取平台角色权限菜单栏-只包含平台")
    public R getRoleAdminPermission() {
        RolePermissionResponse response = this.roleService.getRoleAdminPermission();
        return R.ok(response);
    }

    @PostMapping("org-role-permission")
    @RequiresPermissions(value = {"System:Management:Plat:Role", "System:Management:Plat:Organization"}, logical = Logical.OR)
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
    @RequiresPermissions("System:Management:Plat:Role")
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
    @RequiresPermissions("System:Management:Plat:Role:Edit")
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
    @RequiresPermissions("System:Management:Plat:Role:Create")
    @Log(title = "平台管理新增角色", businessType = BusinessType.INSERT)
    @ApiOperation("平台管理新增角色")
    public R addAdminRole(@RequestBody RoleAddRequest request) {
        this.roleService.addAdminRole(request);
        return R.ok();
    }

    /**
     * 删除数据
     *
     * @return 删除结果
     */
    @PostMapping("delete")
    @RequiresPermissions("System:Management:Plat:Role:Delete")
    @Log(title = "删除【系统角色】", businessType = BusinessType.DELETE)
    @ApiOperation("角色删除")
    public R delete(@RequestBody IdListRequest request) {
        return R.ok(this.roleService.deleteByIds(request.getIdList()));
    }

    /**
     * 批量启用/禁用
     *
     *
     *
     */
    @PostMapping("batch-enabled")
    @RequiresPermissions("System:Management:Plat:Role:Enabled")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.roleService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }
}

