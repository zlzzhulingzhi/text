package cn.qbs.wa.teach.organization.controller.inner;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.organization.entity.Organization;
import cn.qbs.wa.teach.organization.entity.Student;
import cn.qbs.wa.teach.organization.pojo.student.*;
import cn.qbs.wa.teach.organization.service.OrganizationService;
import cn.qbs.wa.teach.organization.service.StudentService;
import cn.qbs.wa.teach.organization.service.inner.StudentInnerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student/inner")
public class StudentInnerController {

    @Resource
    private StudentInnerService studentInnerService;

    @Resource
    private StudentService studentService;

    @Resource
    private OrganizationService organizationService;

    @Value("${system.protocol:https://}")
    private String httpProtocol;

    @Value("${system.primaryHost:cwasp.com}")
    private String primaryHost;

    @AccessAuth({SecurityConstants.INNER})
    @AutoSelectOrg
    @PostMapping("/register")
    public R<Student> register(@RequestBody StudentAddRequest request) {
        return R.ok(this.studentService.registerFromOtherSystem(request));
    }

    @AccessAuth({SecurityConstants.INNER})
    @AutoSelectOrg
    @PostMapping("/loginByUserID")
    public R<LoginUser> getLoginInfoByUserID(@RequestBody @Validated IdOrgRequest request) {
        LoginUser loginUser = this.studentService.getLoginInfoByUserID(request.getId());
        Organization org = organizationService.getById(request.getOrgId());
        if (org != null && StrUtil.isNotBlank(org.getDomain())) {
            loginUser.setDomain(httpProtocol + org.getDomain() + StrUtil.DOT + primaryHost);
        }
        return R.ok(loginUser);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/getStudentName")
    public R<StudentDetailResponse> getStudentName(@RequestBody Student request) {
        return R.ok(this.studentInnerService.getStudentName(request));
    }

    @AccessAuth({SecurityConstants.INNER})
    @ApiOperation("新增学员")
    @PostMapping("/add")
    public R<Student> add(@RequestBody @Validated TCourseStudentAddRequest params) {
        return R.ok(this.studentInnerService.add(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/login-socials")
    @ApiOperation("查询学生信息")
    public R<StudentInfoResponse> getLoginInfoFromSocials(@RequestBody SocialLoginInfoRequest request) {
        StudentInfoResponse student = this.studentInnerService.getLoginInfoFromSocials(request);
        if(student!=null){
            student.setPhone(AesUtil.decode(student.getPhone()));
        }
        return R.ok(student);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/getStu")
    public R<List<Student>> getStu(@RequestBody Student request) {
        List<Student> students=new ArrayList<>();
        if(StringUtils.isNotBlank(request.getRealName())){
            students=studentInnerService.lambdaQuery().like(Student::getRealName,request.getRealName()).eq(Student::getOrgId,request.getOrgId()).list();
        }else {
            students=studentInnerService.lambdaQuery().eq(Student::getOrgId,request.getOrgId()).list();
        }
        return R.ok(students);
    }


}
