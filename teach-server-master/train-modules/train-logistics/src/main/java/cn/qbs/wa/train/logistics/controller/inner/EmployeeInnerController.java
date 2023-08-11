package cn.qbs.wa.train.logistics.controller.inner;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.UpdateBindUserDTO;
import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.pojo.employee.*;
import cn.qbs.wa.train.logistics.pojo.organization.inner.UpdateBindUserRequest;
import cn.qbs.wa.train.logistics.pojo.student.LoginInfoResponse;
import cn.qbs.wa.train.logistics.service.inner.EmployeeInnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;


/**
 * 职工(Employee)表控制层
 *
 * @author makejava
 * @since 2021-11-09 20:11:20
 */
@RestController
@RequestMapping("inner/employee")
@Api(tags = "职工用户管理")
public class EmployeeInnerController {

    /**
     * 服务对象
     */
    @Resource
    private EmployeeInnerService employeeInnerService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("add")
    @ApiOperation("新增职工")
    public R<Employee> add(@RequestBody EmployeeAddRequest params) {
        Employee employee = this.employeeInnerService.add(params);
        return R.ok(employee);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("admin/update")
    @ApiOperation("管理员更新职工")
    public R adminUpdate(@RequestBody @Validated EmployeeUpdateRequest params) {
        EmployeeUpdateResponse updateResponse = this.employeeInnerService.update(params);
        return R.ok();
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/login-info")
    public R<LoginInfoResponse> getLoginInfo(@RequestBody IdRequest request) {
        List<Employee> list = employeeInnerService.lambdaQuery().select(Employee::getId, Employee::getOrgId).eq(Employee::getUserId, request.getId()).eq(Employee::getEnabled, Constants.DB_TRUE).list();
        if (!list.isEmpty()) {
            Employee employee = list.get(0);
            LoginInfoResponse response = new LoginInfoResponse();
            response.setEmployeeId(employee.getId());
            response.setOrgId(employee.getOrgId());
            return R.ok(response);
        }
        return R.ok();

    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/updateBindUser")
    public R<Boolean> updateBindUser(@RequestBody UpdateBindUserRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.employeeInnerService.updateBindUser(params));
    }

}

