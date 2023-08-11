package cn.qbs.wa.teach.basic.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.basic.enums.InitRoleEnum;
import cn.qbs.wa.teach.basic.pojo.user.*;
import cn.qbs.wa.teach.basic.service.UserRoleService;
import cn.qbs.wa.teach.basic.service.UserService;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
public class UserController {

    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Resource
    private UserRoleService userRoleService;


    @PostMapping("add")
    // @RequiresPermissions("user:add")
    @Log(title = "平台新增", businessType = BusinessType.INSERT)
    @ApiOperation("平台管理新增用户")
    public R addUserAdmin(@RequestBody @Validated UserAdminAddRequest request) {
        return R.ok(this.userService.saveUserAdmin(request));
    }

    @PostMapping("inner/add")
    // @RequiresPermissions("user:add")
    @Log(title = "平台新增", businessType = BusinessType.INSERT)
    @ApiOperation("平台管理内部新增用户")
    public R addUserAdminDefault(@RequestBody UserAdminAddRequest request) {
        return R.ok(this.userService.saveUserAdminDefault(request));
    }

    @PostMapping("org/add")
    // @RequiresPermissions("user:add")
    @Log(title = "机构新增", businessType = BusinessType.INSERT)
    @ApiOperation("机构管理新增用户")
    public R addUserOrg(@RequestBody @Validated UserOrgAddRequest request) {
        return R.ok(this.userService.saveUserOrg(request));
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @PostMapping("page")
    //  @RequiresPermissions("user:page")
    @Log(title = "分页查询", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<UserPageResponse>> pageUser(@RequestBody UserPageRequest request) {
        IPage<UserPageResponse> page = this.userService.pageUser(request);
        // 非管理员的角色不允许编辑、机构管理员自己不允许编辑自己
        Long currUserId = SecurityContextHolder.getUserId();
        List<Role> roles = userRoleService.listRoleByUserId(currUserId);
        boolean isOrgMaster = !roles.isEmpty() && roles.stream().filter(r -> StrUtil.isNotBlank(r.getCode())).map(Role::getCode).anyMatch(InitRoleEnum.ADMIN.getCode()::equals);
        Integer priority = null;
        if (CollUtil.isNotEmpty(roles)) {
            priority = roles.stream().map(Role::getPriority).distinct().max(Comparator.naturalOrder()).get();
        }
        for (UserPageResponse record : page.getRecords()) {
            if (isOrgMaster) {
                record.setEditable(true);
            } else {
                // 不能编辑比当前操作人员角色高的角色
                if (record.getId().equals(currUserId)) {
                    record.setEditable(false);
                } else if (priority == null || priority.compareTo(record.getPriority()) < 0) {
                    record.setEditable(false);
                } else {
                    record.setEditable(true);
                }
            }
        }
        return R.ok(page);
    }


    /**
     * 查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @PostMapping("list")
    //  @RequiresPermissions("user:page")
    @Log(title = "列表查询", businessType = BusinessType.OTHER)
    @ApiOperation("列表查询")
    public R<List<UserListResponse>> listUser(@RequestBody UserListRequest request) {
        return R.ok(this.userService.listUser(request));
    }

    /**
     * 查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @PostMapping("list-field")
    //  @RequiresPermissions("user:page")
    @Log(title = "字段列表查询", businessType = BusinessType.OTHER)
    @ApiOperation("字段列表查询")
    public R<List<UserListResponse>> listUserByField(@RequestBody UserListFieldRequest request) {
        return R.ok(this.userService.listUserByField(request));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("detail")
    // @RequiresPermissions("user:details")
    @Log(title = "详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R details(@RequestBody IdRequest request) {
        return R.ok(this.userService.getUserById(request.getId()));
    }


    @PostMapping("update")
    // @RequiresPermissions("user:update")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @ApiOperation("平台管理更新用户")
    public R updateAdminUser(@RequestBody UserAdminUpdateRequest request) {
        Long currUserId = SecurityContextHolder.getUserId();
        List<Role> currRoles = userRoleService.listRoleByUserId(currUserId);
        boolean isOrgMaster = !currRoles.isEmpty() && currRoles.stream().filter(r -> StrUtil.isNotBlank(r.getCode())).map(Role::getCode).anyMatch(InitRoleEnum.ADMIN.getCode()::equals);
        // 不能编辑比当前操作人员角色高的角色
        if (!isOrgMaster) {
            // 非管理员的角色不允许编辑、平台管理员自己不允许编辑自己
            if (request.getId().equals(currUserId)) {
                return R.fail("抱歉，无法编辑当前登录人的个人信息");
            }
            // 默认权重为 0
            Integer priority = null;
            if (CollUtil.isNotEmpty(currRoles)) {
                priority = currRoles.stream().map(Role::getPriority).distinct().max(Comparator.naturalOrder()).get();
            }
            if (priority == null) {
                return R.fail("无操作权限");
            }
            // 被编辑用户的角色最高权重
            List<Role> roleList = userRoleService.listRoleByUserId(request.getId());
            if (CollUtil.isNotEmpty(roleList)) {
                Optional<Integer> maxPriority = roleList.stream().map(Role::getPriority).distinct().max(Comparator.naturalOrder());
                if (maxPriority.isPresent() && maxPriority.get().compareTo(priority) > 0) {
                    return R.fail("权限不足");
                }
            }
        }
        this.userService.updateUserAdmin(request);
        return R.ok();
    }


    @PostMapping("org/update")
    // @RequiresPermissions("user:update")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @ApiOperation("机构管理更新用户基础信息")
    public R updateOrgUser(@RequestBody UserOrgUpdateRequest request) {
        return R.ok(this.userService.updateUserOrg(request));
    }

//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @PostMapping
//    // @RequiresPermissions("user:delete")
//    @Log(title = "删除", businessType = BusinessType.DELETE)
//    public R delete(@RequestBody IdListRequest request) {
//        return R.ok(this.userService.removeByIds(request.getIdList()));
//    }


    @PostMapping("/login-info")
    @Log(title = "获取登录用户信息", businessType = BusinessType.OTHER)
    @ApiOperation("获取登录用户信息")
    public R<LoginUser> getUserInfo(@RequestBody UserInfoRequest userInfoRequest) {
        LoginUser loginUser = this.userService.getUserInfo(userInfoRequest);
        return R.ok(loginUser);
    }

    @PostMapping("/login-permission")
    @Log(title = "获取用户菜单权限", businessType = BusinessType.OTHER)
    @ApiOperation("获取用户菜单权限")
    public R<UserLoginPermissionResponse> getUserPermission(@RequestBody UserLoginPermissionRequest request) {
        UserLoginPermissionResponse response = this.userService.getUserPermission(request);
        return R.ok(response);
    }

    @PostMapping("admin/reset-password")
    @Log(title = "平台管理重置密码", businessType = BusinessType.OTHER)
    @ApiOperation("平台管理重置密码")
    public R resetPasswordByAdmin(@RequestBody UserPasswordAdminRequest request) {
        this.userService.resetPasswordByAdmin(request);
        return R.ok();
    }

    @PostMapping("reset-password")
    @Log(title = "用户修改密码", businessType = BusinessType.OTHER)
    @ApiOperation("用户修改密码")
    public R resetPassword(@RequestBody UserPasswordRequest request) {
        this.userService.resetPassword(request);
        return R.ok();
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.userService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

    @PostMapping("check-exist")
    @ApiOperation("检查是否存在用户")
    public R<?> checkExistUser(@RequestBody UserInfoRequest request) {
        return R.ok(this.userService.checkExistUser(request.getAccount()));
    }


}

