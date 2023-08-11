package cn.qbs.wa.train.logistics.controller;/*
package cn.qbs.wa.train.organization.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.train.organization.excel.StudentDataParseResult;
import cn.qbs.wa.train.organization.pojo.employee.PersonalPasswordUpdateRequest;
import cn.qbs.wa.train.organization.pojo.employee.PersonalPhoneUpdateRequest;
import cn.qbs.wa.train.organization.pojo.importrecord.ImportCountResponse;
import cn.qbs.wa.train.organization.pojo.student.*;
import cn.qbs.wa.train.organization.service.StudentService;
import cn.qbs.wa.train.organization.service.impl.AsyncServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

*/
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.train.logistics.pojo.student.StudentDetailResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 学员(Student)表控制层
 *
 * @author makejava
 */

@Api(tags = "学员")
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    RemoteStudentService remoteStudentService;

    @ApiOperation("根据ID查询学员详情,标签,部门")
    @PostMapping("/details")
    public R<StudentDTO> detail(@RequestBody IdOrgRequest request) {
        return R.ok(remoteStudentService.details(request).getRemoteData());
    }

}
/*
    @Resource
    private StudentService studentService;

    @Resource
    private RedisService redisService;

    @Autowired
    private AsyncServiceImpl asyncService;

    */
/**
     * 新增学员
     *
     * @param params
     * @return
     *//*

    @ApiOperation("新增学员/注册")
    @PostMapping("/add")
    //@RequiresPermissions("student:add")
    @Log(title = "新增学员", businessType = BusinessType.INSERT)
    public R<LoginUser> add(@RequestBody @Validated StudentAddRequest params) {
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        return R.ok(this.studentService.add(params));
    }

    */
/**
     * 分页查询学员
     *
     * @param params 请求参数
     * @return 分页列表
     *//*

    @ApiOperation("分页查询学员")
    @PostMapping("/page")
    //@RequiresPermissions("student:page")
    //@Log(title = "分页查询学员", businessType = BusinessType.OTHER)
    public R<IPage<StudentPageResponse>> page(@RequestBody StudentPageRequest params) {
        return R.ok(this.studentService.page(params));
    }

    */
/**
     * 分页查询学员
     *
     * @param params 请求参数
     * @return 分页列表
     *//*

    @ApiOperation("平台管理分页查询学员")
    @PostMapping("/admin/page")
    //@RequiresPermissions("student:page")
    //@Log(title = "分页查询学员", businessType = BusinessType.OTHER)
    public R<IPage<StudentPageResponse>> pageNoTenant(@RequestBody StudentPageRequest params) {
        return R.ok(this.studentService.pageNoTenant(params));
    }

    */
/**
     * 分页查询学员
     *
     * @param params 请求参数
     * @return 分页列表
     *//*

    @ApiOperation("分页查询学员")
    @PostMapping("/staff/page")
    public R<IPage<StudentPageResponse>> pageWithStaff(@RequestBody StudentPageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        //return R.ok(this.studentService.pageWithStaff(params));
        return R.ok(this.studentService.pageV2(params));
    }

    */
/**
     * 查询学员列表
     *
     * @param params 请求参数
     * @return 列表
     *//*

    @ApiOperation("查询学员列表")
    @PostMapping("/list")
    //@RequiresPermissions("student:list")
    //@Log(title = "查询学员列表", businessType = BusinessType.OTHER)
    public R<List<StudentListResponse>> list(@RequestBody StudentListRequest params) {
        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        return R.ok(this.studentService.list(params));
    }

    */
/**
     * 查询学员详情
     *
     * @param request 主键
     * @return
     *//*

    @AutoSelectOrg
    @ApiOperation("根据ID查询学员详情")
    @PostMapping("/detail")
    //@RequiresPermissions("student:details")
    //@Log(title = "学员详情", businessType = BusinessType.OTHER)
    public R<StudentDetailResponse> detail(@RequestBody IdOrgRequest request) {
        Long studentId = request.getId() == null ? SecurityContextHolder.getStudentId() : request.getId();
        return R.ok(this.studentService.detail(studentId));
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
        boolean update = this.studentService.update(params);
        if (update) {
            // 学员禁用，退出登录
            if (Constants.DB_FAIL.equals(params.getEnabled())) {
                asyncService.loggingOff(AsyncServiceImpl.LogOutType.STUDENT, params.getId());
            }
        }
        return R.ok(update);
    }

    */
/**
     * 删除学员
     *
     * @param request 主键集合
     * @return
     *//*

    @ApiOperation("删除学员")
    @PostMapping("/delete")
    //@RequiresPermissions("student:delete")
    //@Log(title = "删除学员", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.studentService.deleteByIds(request.getIdList()));
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

    @PostMapping("/login-social")
    @ApiOperation("学员登录授权")
    public R<LoginUser> getLoginInfoFromSocial(@RequestBody SocialLoginInfoRequest request) {
        LoginUser loginUser = this.studentService.getLoginInfoFromSocial(request);
        return R.ok(loginUser);
    }

    @PostMapping("/binding-social")
    @ApiOperation("绑定第三方账号")
    public R<LoginUser> bindingSocial(@RequestBody SocialBindingRequest request) {
        if (request.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        } else {
            request.setUserId(SecurityContextHolder.getUserId());
            request.setOrgId(SecurityContextHolder.getOrgId());
        }
        LoginUser loginUser = this.studentService.bindingSocial(request);
        return R.ok(loginUser);
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
        //if (result) {
        //    // 注销当前登录信息，重新登录
        //    AuthUtil.logoutByToken(SecurityUtils.getToken());
        //}
        return R.ok(result);
    }

    @ApiOperation("重置密码")
    @PostMapping("/reset-password")
    //@RequiresPermissions("student:update")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    public R<Boolean> resetPassword(@RequestBody PasswordResetRequest params) {
        if (SecurityContextHolder.getStudentId() != null) {
            Long orgId = SecurityContextHolder.getOrgId();
            String account = params.getAccount();
            checkCode(orgId, account, CacheConstants.FORGET_PWD_CODE, params.getCode());
            redisService.deleteObject(getCacheKey(CacheConstants.FORGET_PWD_CODE, orgId, account));
            params.setOrgId(orgId);
        } else {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        }
        Boolean result = this.studentService.resetPassword(params);
        return R.ok(result);
    }

    @SneakyThrows
    @ApiOperation("导入学员-预览")
    @PostMapping("/import/preview")
    public R<List<StudentDataParseResult>> importPreview(MultipartFile file) {
        */
/*ExcelDataListener<StudentData> dataListener = new ExcelDataListener<>();
        EasyExcel.read(file.getInputStream(), StudentData.class, dataListener).sheet().doRead();
        List<StudentData> dataList = dataListener.getDataList();
        List<StudentImportRequest> collect = dataList.stream().map(e -> {
            StudentImportRequest studentImportRequest = BeanUtil.copyProperties(e, StudentImportRequest.class);
            studentImportRequest.setAccount(studentImportRequest.getPhone());
            studentImportRequest.setSex(Optional.ofNullable(SexEnum.getEnum(e.getSexName())).orElse(SexEnum.OTHER).getSex());
            return studentImportRequest;
        }).collect(Collectors.toList());
        return R.ok(collect);*//*

        return R.ok(this.studentService.importPreview(file));
    }


    @ApiOperation("导入学员-保存")
    @PostMapping("/import/save")
    public R<ImportCountResponse> importSave(@RequestBody @NotEmpty(message = "导入数据不为空") List<StudentImportRequest> list) {
        List<StudentAddRequest> collect = list.stream().map(e -> {
            StudentAddRequest studentAddRequest = BeanUtil.copyProperties(e, StudentAddRequest.class);
            //效验身份证是否为18位
            String idNumber = studentAddRequest.getIdNumber();
            if (StringUtils.isNotEmpty(idNumber)){
                if (idNumber.length()!=18){
                    studentAddRequest.setIdNumber(null);
                }
            }
            studentAddRequest.setAccount(studentAddRequest.getPhone());
            studentAddRequest.setNickName(studentAddRequest.getRealName());
            studentAddRequest.setPhone(studentAddRequest.getPhone());
            return studentAddRequest;
        }).collect(Collectors.toList());
        return R.ok(this.studentService.batchSave(collect));
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

    */
/**
     * 查询学员详情
     *
     * @param request 主键
     * @return
     *//*

    @AutoSelectOrg
    @ApiOperation("根据ID查询学员详情,异步查询")
    @PostMapping("/detailNoTenant")
    //@RequiresPermissions("student:details")
    //@Log(title = "学员详情", businessType = BusinessType.OTHER)
    public R<StudentDetailResponse> detailNoTenant(@RequestBody @Validated IdOrgRequest request) {
        return R.ok(this.studentService.selectDetailByIdNoTenant(request));
    }

    */
/**
     * 查询学员详情
     *
     * @param request 主键
     * @return
     *//*

    @AutoSelectOrg
    @ApiOperation("根据ID查询学员详情,标签,部门")
    @PostMapping("/details")
    //@RequiresPermissions("student:details")
    //@Log(title = "学员详情", businessType = BusinessType.OTHER)
    public R<StudentDetailResponse> details(@RequestBody @Validated IdOrgRequest request) {
        return R.ok(this.studentService.details(request.getId()));
    }

    @PostMapping("/category/count")
    @ApiOperation(value = "获取分类下的数量")
    public R<Long> categoryCourseCount(@RequestBody @Validated IdListParam param) {
        return R.ok(this.studentService.categoryCount(param.getIdList()));
    }
}

*/
