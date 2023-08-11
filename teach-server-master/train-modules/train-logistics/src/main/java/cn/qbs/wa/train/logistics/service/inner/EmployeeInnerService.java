package cn.qbs.wa.train.logistics.service.inner;

import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.pojo.employee.*;
import cn.qbs.wa.train.logistics.pojo.organization.inner.UpdateBindUserRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 职工(Employee)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:11:18
 */
public interface EmployeeInnerService extends IService<Employee> {

    Employee add(EmployeeAddRequest params);

    EmployeeUpdateResponse update(EmployeeUpdateRequest params);

    Boolean updateBindUser(UpdateBindUserRequest params);
}

