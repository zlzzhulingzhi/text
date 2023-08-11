package cn.qbs.wa.train.logistics.controller;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.pojo.employee.EmployeeLoginPermissionResponse;
import cn.qbs.wa.train.logistics.pojo.index.EmployeeLoginRequest;
import cn.qbs.wa.train.logistics.pojo.index.OrgInfoResponse;
import cn.qbs.wa.train.logistics.service.EmployeeService;
import cn.qbs.wa.train.logistics.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yjx
 */
@Api(tags = "机构后台-登录首页")
@RefreshScope
@RestController
public class IndexController {

    @Resource
    private IndexService indexService;

    @Resource
    private EmployeeService employeeService;

    @ApiOperation("机构列表")
    @PostMapping("/org-list")
    public R<List<OrgInfoResponse>> listOrg() {
        Long userId = SecurityContextHolder.getUserId();
        List<OrgInfoResponse> orgList = indexService.listOrg(userId);
        return R.ok(orgList);
    }

    @ApiOperation("职工菜单权限")
    @PostMapping("/login-permission")
    public R<EmployeeLoginPermissionResponse> getUserPermission(@RequestBody EmployeeLoginRequest request) {
        Long userId = SecurityContextHolder.getUserId();
        Employee employee = employeeService.lambdaQuery().eq(Employee::getOrgId, request.getOrgId()).eq(Employee::getUserId, userId).one();
        if (employee == null) {
            return R.ok(new EmployeeLoginPermissionResponse());
        }
        if (Constants.DB_FAIL.equals(employee.getEnabled())) {
            return R.fail("该员工账号已被禁用，请联系机构管理员");
        }
        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
        return R.ok(employeeService.getEmployeePermission(request.getApplicationTypeId(), employee));
    }

}
