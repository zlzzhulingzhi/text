package cn.qbs.wa.union.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.enums.InitRoleEnum;
import cn.qbs.wa.union.admin.pojo.systemuser.*;
import cn.qbs.wa.union.admin.service.InnerSystemUserService;
import cn.qbs.wa.union.admin.service.SystemUserRoleService;
import cn.qbs.wa.union.admin.service.SystemUserService;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 平台系统子用户表(SystemUser)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
@RestController
@RequestMapping("systemUser")
@Api(tags = "统一认证管理用户")
public class SystemUserController {

    /**
     * 服务对象
     */
    @Resource
    private SystemUserService systemUserService;

    @Resource
    private InnerSystemUserService innerSystemUserService;

    @Resource
    private SystemUserRoleService systemUserRoleService;


    /**
     * 新增平台系统子用户表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("System:Admin:User")
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated SystemUserAddRequest params) {
        //这里account需要加密
        params.setAccount(AesUtil.encode(params.getAccount()));
        params.setPhone(AesUtil.encode(params.getPhone()));
        params.setPassword(SecurityUtils.encryptPassword(params.getPassword()));
        return R.ok(this.systemUserService.add(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("inner-add")
    @ApiOperation("内部新增")
    public R innerAdd(@RequestBody @Validated InnerSystemUserAddRequest params) {
        //这里account需要加密
        params.setAccount(AesUtil.encode(params.getAccount()));
        params.setPhone(AesUtil.encode(params.getPhone()));
        params.setPassword(SecurityUtils.encryptPassword(params.getPassword()));
        return R.ok(this.innerSystemUserService.innerAdd(params));
    }

    /**
     * 分页查询平台系统子用户表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions("System:Admin:User")
    public R<IPage<SystemUserPageResponse>> page(@RequestBody SystemUserPageRequest params) {
        IPage<SystemUserPageResponse> page = this.systemUserService.page(params);
        // 非管理员的角色不允许编辑、机构管理员自己不允许编辑自己
        Long currUserId = SecurityContextHolder.getUserId();
        List<SystemRole> roles = systemUserRoleService.listRoleByUserId(currUserId);
        boolean isOrgMaster = !roles.isEmpty() && roles.stream().filter(r -> StrUtil.isNotBlank(r.getCode())).map(SystemRole::getCode).anyMatch(InitRoleEnum.ADMIN.getCode()::equals);
        Integer priority = null;
        if (CollUtil.isNotEmpty(roles)) {
            priority = roles.stream().map(SystemRole::getPriority).distinct().max(Comparator.naturalOrder()).get();
        }
        for (SystemUserPageResponse record : page.getRecords()) {
            if (isOrgMaster) {
                record.setEditable(true);
            } else {
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
     * 查询平台系统子用户表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @RequiresPermissions("System:Admin:User")
    public R<SystemUserDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.systemUserService.detail(request.getId()));
    }

    @PostMapping("/info")
    public R<SystemUserDetailResponse> info() {
        if (!UserConstants.USER_PLATFORM.equals(SecurityContextHolder.getUserType())) {
            return null;
        }
        Long userId = SecurityContextHolder.getUserId();
        return R.ok(this.systemUserService.detail(userId));
    }

    /**
     * 修改平台系统子用户表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @RequiresPermissions("System:Admin:User")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated SystemUserUpdateRequest params) {
        Long currUserId = SecurityContextHolder.getUserId();
        List<SystemRole> currRoles = systemUserRoleService.listRoleByUserId(currUserId);
        boolean isOrgMaster = !currRoles.isEmpty() && currRoles.stream().filter(r -> StrUtil.isNotBlank(r.getCode())).map(SystemRole::getCode).anyMatch(InitRoleEnum.ADMIN.getCode()::equals);
        // 不能编辑比当前操作人员角色高的角色
        if (!isOrgMaster) {
            // 非管理员的角色不允许编辑、平台管理员自己不允许编辑自己
            if (params.getId().equals(currUserId)) {
                return R.fail("抱歉，无法编辑当前登录人的个人信息");
            }
            // 默认权重为 0
            Integer priority = null;
            if (CollUtil.isNotEmpty(currRoles)) {
                priority = currRoles.stream().map(SystemRole::getPriority).distinct().max(Comparator.naturalOrder()).get();
            }
            if (priority == null) {
                return R.fail("无操作权限");
            }
            // 被编辑用户的角色最高权重
            List<SystemRole> roleList = systemUserRoleService.listRoleByUserId(params.getId());
            if (CollUtil.isNotEmpty(roleList)) {
                Optional<Integer> maxPriority = roleList.stream().map(SystemRole::getPriority).distinct().max(Comparator.naturalOrder());
                if (maxPriority.isPresent() && maxPriority.get().compareTo(priority) > 0) {
                    return R.fail("权限不足");
                }
            }
        }
        params.setAccount(AesUtil.encode(params.getAccount()));
        params.setPhone(AesUtil.encode(params.getPhone()));
        return R.ok(this.systemUserService.update(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("inner-update")
    @ApiOperation("更新")
    public R innerUpdate(@RequestBody @Validated InnerSystemUserUpdateRequest params) {
        this.systemUserService.innerUpdate(params);
        return R.ok(true);
    }

    @PostMapping("/menu-permission")
    @ApiOperation("获取菜单权限")
    public R<SystemUserRoleMenuMineResponse> getMinePermission() {
        return R.ok(systemUserService.getMinePermission());
    }

}

