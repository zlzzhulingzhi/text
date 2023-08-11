package cn.qbs.wa.train.logistics.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.entity.EmployeeInfo;
import cn.qbs.wa.train.logistics.entity.OrganizationRole;
import cn.qbs.wa.train.logistics.enums.InitRoleEnum;
import cn.qbs.wa.train.logistics.pojo.employee.*;
import cn.qbs.wa.train.logistics.service.*;
import cn.qbs.wa.train.logistics.service.impl.AsyncServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


/**
 * 职工(Employee)表控制层
 *
 * @author makejava
 * @since 2021-11-09 20:11:20
 */
@RestController
@RequestMapping("employee")
@Api(tags = "职工用户管理")
public class EmployeeController {

    /**
     * 服务对象
     */
    @Resource
    private EmployeeService employeeService;

    @Resource
    private EmployeeInfoService employeeInfoService;

    @Resource
    private EmployeeRoleService employeeRoleService;

    @Resource
    private RedisService redisService;

    @Autowired
    private AsyncServiceImpl asyncService;

    /**
     * 新增职工
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("employee:add")
    @ApiOperation("新增职工")
    public R<Employee> add(@RequestBody @Validated EmployeeAddRequest params) {
        params.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        params.setAccount(params.getPhone());
        Employee employee = this.employeeService.add(params);
        return R.ok(employee);
    }

    /**
     * 新增职工
     *
     * @param params
     * @return
     */
    @PostMapping("admin/add")
    //@RequiresPermissions("employee:add")
    @ApiOperation("管理员新增职工")
    public R<Employee> adminAdd(@RequestBody @Validated EmployeeAddRequest params) {
        Employee employee = this.employeeService.add(params);
        return R.ok(employee);
    }

    /**
     * 分页查询职工
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("employee:page")
    @ApiOperation("机构管理员分页查询职工")
    public R<IPage<EmployeePageResponse>> page(@RequestBody EmployeePageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        IPage<EmployeePageResponse> page = this.employeeService.page(params);
        // 非机构管理员的角色不允许编辑、机构管理员自己不允许编辑自己
        Long currEmpId = SecurityContextHolder.getEmployeeId();
        List<OrganizationRole> roles = employeeRoleService.getRole(currEmpId);
        boolean isOrgMaster = roles.isEmpty() || roles.stream().filter(r -> StrUtil.isNotBlank(r.getCode())).map(OrganizationRole::getCode).noneMatch(InitRoleEnum.ORG_MASTER.getCode()::equals);
        for (EmployeePageResponse record : page.getRecords()) {
            if (isOrgMaster) {
                record.setEditable(false);
            } else {
                record.setEditable(!record.getId().equals(currEmpId));
            }
        }
        return R.ok(page);
    }

    /**
     * 管理员分页查询职工
     *
     * @param params
     * @return
     */
    @PostMapping("admin/page")
    //@RequiresPermissions("employee:page")
    @ApiOperation("平台管理员分页查询职工")
    public R<IPage<EmployeePageResponse>> adminPage(@RequestBody EmployeePageRequest params) {
        return R.ok(this.employeeService.page(params));
    }

    @PostMapping("admin/list")
    //@RequiresPermissions("employee:page")
    @ApiOperation("管理员职工列表")
    public R<List<EmployeePageResponse>> adminList(@RequestBody EmployeeListRequest params) {
        return R.ok(this.employeeService.listEmployee(params));
    }

    @PostMapping("list")
    //@RequiresPermissions("employee:page")
    @ApiOperation("职工列表")
    public R<List<EmployeePageResponse>> list(@RequestBody EmployeeListRequest params) {
        params.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        return R.ok(this.employeeService.listEmployee(params));
    }

    @PostMapping("list-full")
    //@RequiresPermissions("employee:page")
    @ApiOperation("职工列表包含角色部门信息")
    public R<List<EmployeePageResponse>> listFull(@RequestBody EmployeeListRequest params) {
        params.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        return R.ok(this.employeeService.listEmployeeFull(params));
    }


    /**
     * 查询职工详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("employee:details")
    //@Log(title = "职工详情", businessType = BusinessType.OTHER)
    @ApiOperation("职工详情")
    public R<EmployeeDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.employeeService.detail(request.getId()));
    }

    /**
     * 修改职工
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("employee:update")
    @ApiOperation("机构更新职工")
    public R update(@RequestBody @Validated EmployeeUpdateRequest params) {
        params.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        EmployeeUpdateResponse updateResponse = this.employeeService.update(params);
        // 职工禁用，退出登录
        if (Constants.DB_FAIL.equals(params.getEnabled())) {
            asyncService.loggingOff(AsyncServiceImpl.LogOutType.EMPLOYEE, params.getId());
        }
        return R.ok();
    }

    /**
     * 修改职工
     *
     * @param params
     * @return
     */
    @PostMapping("admin/update")
    //@RequiresPermissions("employee:update")
    @ApiOperation("管理员更新职工")
    public R adminUpdate(@RequestBody @Validated EmployeeUpdateRequest params) {
        EmployeeUpdateResponse updateResponse = this.employeeService.update(params);
        return R.ok();
    }

//    /**
//     * 删除职工
//     *
//     * @param idList 主键集合
//     * @return
//     */
//    @PostMapping("delete")
//    //@RequiresPermissions("employee:delete")
//    //@Log(title = "删除职工", businessType = BusinessType.DELETE)
//    public R<Boolean> delete(@RequestBody IdListRequest request) {
//        this.employeeService.deleteByIds(request.getIdList())
//        return R.ok();
//    }


    @PostMapping("/login-permission")
    @ApiOperation("获取职工菜单权限")
    public R<EmployeeLoginPermissionResponse> getUserPermission(@RequestBody EmployeeLoginPermissionRequest request) {
        EmployeeLoginPermissionResponse response = this.employeeService.getEmployeePermission(request);
        return R.ok(response);
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.employeeService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }


    /**
     * 批量启用/禁用
     */
    @PostMapping("admin/batch-enabled")
    //@RequiresPermissions("role:delete")
    @ApiOperation("批量启用/禁用")
    public R batchAdminEnabled(@RequestBody BatchFlagRequest request) {
        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        this.employeeService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

    @PostMapping("reset-password")
    @ApiOperation("机构管理重置密码")
    public R resetPassword(@RequestBody IdRequest request) {
        this.employeeService.resetPassword(request);
        return R.ok();
    }

    /**
     * 查询职工详情
     *
     * @param request user_id
     * @return
     */
    @PostMapping("/personal-info")
    //@RequiresPermissions("employee:details")
    //@Log(title = "职工详情", businessType = BusinessType.OTHER)
    @ApiOperation("查询职工个人信息")
    public R<EmployeeDetailResponse> personalInfo(@RequestBody IdRequest request) {
        return R.ok(this.employeeService.personalInfoByUserId(request.getId()));
    }

    /**
     * 修改个人信息
     */
    @ApiOperation("修改个人信息")
    @PostMapping("/change-info")
    //@RequiresPermissions("employee:update")
    public R<Boolean> changeInfo(@RequestBody @Validated PersonalInfoUpdateRequest params) {
        return R.ok(this.employeeService.changeInfoByPersonal(params));
    }

    /**
     * 修改手机号码
     */
    @ApiOperation("修改手机号码")
    @PostMapping("/change-phone")
    //@RequiresPermissions("employee:update")
    public R<Boolean> changePhone(@RequestBody @Validated PersonalPhoneUpdateRequest params) {
        Object cacheCode = redisService.getCacheObject(CacheConstants.CHANGE_PHONE_CODE + params.getPhone());
        if (cacheCode == null) {
            throw new ServiceException("验证码已过期请重新再试");
        }
        String code = cacheCode.toString();
        if (!code.equals(params.getCode())) {
            throw new ServiceException("验证码错误,请重新输入");
        }
        Boolean result = this.employeeService.changePhoneByPersonal(params);
        if (result) {
            redisService.deleteObject(CacheConstants.CHANGE_PHONE_CODE + params.getPhone());
        }
        return R.ok(result);
    }

    /**
     * 修改个人密码
     */
    @ApiOperation("修改个人密码")
    @PostMapping("/change-password")
    //@RequiresPermissions("employee:update")
    public R<Boolean> changePassword(@RequestBody @Validated PersonalPasswordUpdateRequest params) {
        Boolean result = this.employeeService.changePasswordByPersonal(params);
        if (result) {
            // 注销当前登录信息，重新登录
            AuthUtil.logoutByToken(SecurityUtils.getToken());
        }
        return R.ok(result);
    }


    /**
     * 机构更新职工角色
     *
     * @param params
     * @return
     */
    @PostMapping("update-role")
    //@RequiresPermissions("employee:update")
    @ApiOperation("机构更新职工角色")
    public R updateRole(@RequestBody @Validated EmployeeUpdateRoleRequest params) {
        params.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        this.employeeService.updateRole(params);
        return R.ok();
    }

    /**
     * 管理员更新职工角色
     *
     * @param params
     * @return
     */
    @PostMapping("admin/update-role")
    //@RequiresPermissions("employee:update")
    @ApiOperation("管理员更新职工角色")
    public R adminUpdateRole(@RequestBody @Validated EmployeeUpdateRoleRequest params) {
        this.employeeService.updateRole(params);
        return R.ok();
    }

    /**
     * 注册新增职工
     *
     * @param params 注册参数
     * @return 职员信息
     */
    @PostMapping("/register")
    //@RequiresPermissions("employee:add")
    @ApiOperation("注册新增职工")
    public R<Employee> register(@RequestBody EmployeeSpecAddRequest params) {
        Long orgId = Optional.ofNullable(params.getOrgId()).orElse(SecurityContextHolder.getOrgId());
        SecurityContextHolder.setSelectOrgId(orgId.toString());
        if (!orgId.equals(params.getOrgId())) {
            params.setOrgId(orgId);
        }
        Employee employee = this.employeeService.register(params);
        return R.ok(employee);
    }

    /**
     * 查询职工详情
     *
     * @param request user_id
     * @return
     */
    @PostMapping("/personal-info/v2")
    //@RequiresPermissions("employee:details")
    //@Log(title = "职工详情", businessType = BusinessType.OTHER)
    @ApiOperation("查询职工个人信息")
    public R<EmployeeSpecDetailResponse> personalInfoV2(@RequestBody IdRequest request) {
        EmployeeDetailResponse employeeDetailResponse = this.employeeService.personalInfoByUserId(request.getId());
        EmployeeSpecDetailResponse employeeSpecDetailResponse = new EmployeeSpecDetailResponse();
        if (employeeDetailResponse != null) {
            BeanUtil.copyProperties(employeeDetailResponse, employeeSpecDetailResponse);
            employeeInfoService.lambdaQuery().eq(EmployeeInfo::getEmployeeId, employeeDetailResponse.getId()).oneOpt().ifPresent(e -> {
                employeeSpecDetailResponse.setEducation(e.getEducation());
                employeeSpecDetailResponse.setJob(e.getJob());
                employeeSpecDetailResponse.setPosition(e.getPosition());
            });
        }
        return R.ok(employeeSpecDetailResponse);
    }

    /**
     * 修改个人信息
     */
    @ApiOperation("修改个人信息")
    @PostMapping("/change-info/v2")
    //@RequiresPermissions("employee:update")
    public R<Boolean> changeInfoV2(@RequestBody @Validated PersonalInfoSpecUpdateRequest params) {
        return R.ok(this.employeeService.changeInfoByPersonalV2(params));
    }

    @PostMapping("/category/count")
    @ApiOperation(value = "获取分类下的数量")
    public R<Long> categoryCourseCount(@RequestBody @Validated IdListParam param) {
        return R.ok(this.employeeService.categoryCount(param.getIdList()));
    }

    @PostMapping("/count")
    @ApiOperation("查询职工数量")
    public R<Long> employeeCount(@RequestBody @Validated PlatformIdListParam param) {
        return R.ok(this.employeeService.lambdaQuery().eq(Employee::getOrgId, param.getOrgId()).count());
    }

    @PostMapping("getOrgList")
    @ApiOperation("获取机构职工列表")
    public R<List<Employee>> getOrgList(@RequestBody EmployeeListRequest params) {
        List<Employee> employeeList=employeeService.lambdaQuery().eq(Employee::getOrgId,params.getOrgId()).eq(Employee::getEnabled,Constants.DB_TRUE).list();
        return R.ok(employeeList);
    }
}

