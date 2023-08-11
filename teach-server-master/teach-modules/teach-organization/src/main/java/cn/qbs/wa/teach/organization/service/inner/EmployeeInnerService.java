package cn.qbs.wa.teach.organization.service.inner;

import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.pojo.employee.*;
import cn.qbs.wa.teach.organization.pojo.organization.inner.UpdateBindUserRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 职工(Employee)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:11:18
 */
public interface EmployeeInnerService extends IService<Employee> {

    Employee add(EmployeeAddRequest params);

    Boolean updateBindUser(UpdateBindUserRequest params);
}

