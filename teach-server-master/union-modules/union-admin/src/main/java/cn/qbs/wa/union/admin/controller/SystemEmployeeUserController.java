package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.union.admin.pojo.systememployeeuser.InnerEmployeeUserAddRequest;
import cn.qbs.wa.union.admin.pojo.systememployeeuser.InnerEmployeeUserUpdateRequest;
import cn.qbs.wa.union.admin.pojo.systememployeeuser.SystemEmployeeUserDetailResponse;
import cn.qbs.wa.union.admin.service.SystemEmployeeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * 职工用户表(SystemEmployeeUser)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:03
 */
@RestController
@RequestMapping("systemEmployeeUser")
@Api(tags = "职工用户")
public class SystemEmployeeUserController {

    /**
     * 服务对象
     */
    @Resource
    private SystemEmployeeUserService systemEmployeeUserService;



    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("inner-add")
    @ApiOperation("内部新增")
    public R innerAdd(@RequestBody @Validated InnerEmployeeUserAddRequest params) {
        //这里account需要加密
        params.setAccount(AesUtil.encode(params.getAccount()));
        params.setPhone(AesUtil.encode(params.getPhone()));
        params.setPassword(SecurityUtils.encryptPassword(params.getPassword()));
        return R.ok(this.systemEmployeeUserService.innerAdd(params));
    }


    /**
     * 查询职工用户表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("/info")
    //@RequiresPermissions("systemEmployeeUser:details")
    //@Log(title = "职工用户表详情", businessType = BusinessType.OTHER)
    public R<SystemEmployeeUserDetailResponse> info() {
        if (!UserConstants.USER_EMPLOYEE.equals(SecurityContextHolder.getUserType())) {
            return null;
        }
        Long userId = SecurityContextHolder.getUserId();
        return R.ok(this.systemEmployeeUserService.detail(userId));
    }

    //@PostMapping("detail")
    //@RequiresPermissions("systemEmployeeUser:details")
    //@Log(title = "职工用户表详情", businessType = BusinessType.OTHER)
    public R<SystemEmployeeUserDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.systemEmployeeUserService.detail(request.getId()));
    }


    /**
     * 修改职工用户表
     *
     * @param params
     * @return
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("inner-update")
    @ApiOperation("内部更新")
    public R update(@RequestBody @Validated InnerEmployeeUserUpdateRequest params) {
        this.systemEmployeeUserService.innerUpdate(params);
        return R.ok(true);
    }



}

