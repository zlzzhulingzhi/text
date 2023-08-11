package cn.qbs.wa.train.logistics.controller.app;/*
package cn.qbs.wa.train.logistics.controller.app;

import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.logistics.pojo.employee.PersonalPasswordUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.employee.PersonalPhoneUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.student.LoginInfoRequest;
import cn.qbs.wa.train.logistics.pojo.student.StudentDetailResponse;
import cn.qbs.wa.train.logistics.pojo.student.StudentUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.student.app.AppSocialBindingRequest;
import cn.qbs.wa.train.logistics.service.AppStudentService;
import cn.qbs.wa.train.logistics.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

*/
/**
 * 学员(Student)表控制层
 *
 * @author makejava
 *//*

@Api(tags = "移动端-学员")
@RestController
@RequestMapping("/app/student")
public class AppStudentController {

    @Resource
    private StudentService studentService;

    @Resource
    private AppStudentService appStudentService;

    @Resource
    private RedisService redisService;

    */
/**
     * 查询学员详情
     *
     * @param request 主键
     * @return
     *//*

    @ApiOperation("根据ID查询学员详情")
    @PostMapping("/detail")
    //@RequiresPermissions("student:details")
    //@Log(title = "学员详情", businessType = BusinessType.OTHER)
    public R<StudentDetailResponse> detail(@RequestBody @Validated IdRequest request) {
        return R.ok(this.studentService.detail(request.getId()));
    }

    */
/**
     * 修改学员
     *
     * @param params
     * @return
     *//*

    @ApiOperation("修改学员信息")
    @PostMapping("/update")
    //@RequiresPermissions("student:update")
    //@Log(title = "更新学员", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated StudentUpdateRequest params) {
        return R.ok(this.studentService.update(params));
    }

    */
/**
     * 学员登录授权
     *
     * @param loginInfoRequest 主键集合
     * @return
     *//*

    @PostMapping("/login-info")
    @ApiOperation("获取学员登录信息")
    public R<LoginUser> getLoginInfo(@RequestBody @Validated LoginInfoRequest loginInfoRequest) {
        LoginUser loginUser = this.studentService.getLoginInfo(loginInfoRequest);
        return R.ok(loginUser);
    }

    @PostMapping("/binding-social")
    @ApiOperation("绑定第三方账号")
    public R<Boolean> bindingSocial(@RequestBody @Validated AppSocialBindingRequest request) {
        return R.ok(this.appStudentService.bindingSocial(request));
    }

    @PostMapping("/binding-check")
    @ApiOperation("绑定关系校验")
    R<Boolean> getPhoneBindingInfo(@RequestBody LoginInfoRequest request) {
        if (request.getOrgId() == null) {
            request.setOrgId(SecurityContextHolder.getOrgId());
        }
        return R.ok(this.studentService.getPhoneBindingInfo(request));
    }

    @PostMapping("/unbind-social")
    @ApiOperation("解除绑定")
    public R<Boolean> unbindSocial(@RequestBody @Validated IdRequest request) {
        Long studentId = request.getId();
        if (!studentId.equals(SecurityContextHolder.getStudentId())) {
            return R.fail("当前学员信息有误");
        }
        return R.ok(this.studentService.unbindSocial(studentId));
    }

    */
/**
     * 修改手机号码
     *//*

    @ApiOperation("修改手机号码")
    @PostMapping("/change-phone")
    //@RequiresPermissions("student:update")
    @Log(title = "修改手机号码", businessType = BusinessType.UPDATE)
    public R<Boolean> changePhone(@RequestBody @Validated PersonalPhoneUpdateRequest params) {
        String phone = params.getPhone();
        Long orgId = SecurityContextHolder.getOrgId();
        checkCode(orgId, phone, CacheConstants.CHANGE_PHONE_CODE, params.getCode());
        boolean result = this.studentService.changePhone(params);
        if (result) {
            redisService.deleteObject(getCacheKey(CacheConstants.CHANGE_PHONE_CODE, orgId, phone));
        }
        return R.ok(result);
    }

    */
/**
     * 修改个人密码
     *//*

    @ApiOperation("修改个人密码")
    @PostMapping("/change-password")
    //@RequiresPermissions("student:update")
    @Log(title = "修改个人密码", businessType = BusinessType.UPDATE)
    public R<Boolean> changePassword(@RequestBody @Validated PersonalPasswordUpdateRequest params) {
        Boolean result = this.studentService.changePassword(params);
        if (result) {
            // 注销当前登录信息，重新登录
            AuthUtil.logoutByToken(SecurityUtils.getToken());
        }
        return R.ok(result);
    }

    private String getCacheKey(String prefix, Long orgId, String account) {
        return prefix + ":" + orgId + ":" + account;
    }

    private void checkCode(Long orgId, String account, String cachePrefix, String code) {
        Object cacheCode = redisService.getCacheObject(getCacheKey(cachePrefix, orgId, account));
        if (cacheCode == null) {
            throw new ServiceException("验证码已过期请重新再试");
        }
        String c = cacheCode.toString();
        if (!c.equals(code)) {
            throw new ServiceException("验证码错误,请重新输入");
        }
    }
}

*/
