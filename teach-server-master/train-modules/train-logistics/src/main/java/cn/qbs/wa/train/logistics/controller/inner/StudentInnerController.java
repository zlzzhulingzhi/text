package cn.qbs.wa.train.logistics.controller.inner;/*
package cn.qbs.wa.train.logistics.controller.inner;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.entity.Student;
import cn.qbs.wa.train.logistics.pojo.student.StudentAddRequest;
import cn.qbs.wa.train.logistics.service.OrganizationService;
import cn.qbs.wa.train.logistics.service.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student/inner")
public class StudentInnerController {

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
}
*/
