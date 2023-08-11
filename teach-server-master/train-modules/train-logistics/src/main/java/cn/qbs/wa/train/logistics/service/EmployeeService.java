package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.pojo.employee.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 职工(Employee)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:11:18
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 新增职工
     * @param params
     * @return
     */
    Employee add(EmployeeAddRequest params);

    /**
     * 分页查询职工
     * @param params
     * @return
     */
    IPage<EmployeePageResponse> page(EmployeePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    EmployeeDetailResponse detail(Serializable id);

    /**
     * 更新职工
     * @param params
     * @return
     */
    EmployeeUpdateResponse update(EmployeeUpdateRequest params);

    /**
     * 删除职工
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<EmployeePageResponse> listEmployee(EmployeeListRequest params);

    EmployeeLoginPermissionResponse getEmployeePermission(EmployeeLoginPermissionRequest request);

    EmployeeLoginPermissionResponse getEmployeePermission(Long applicationTypeId, Employee employee);

    void batchEnabled(Integer flag, List<Long> idList);

    void resetPassword(IdRequest request);

    List<EmployeePageResponse> listEmployeeFull(EmployeeListRequest params);

    Boolean changeInfoByPersonal(PersonalInfoUpdateRequest params);

    Boolean changePhoneByPersonal(PersonalPhoneUpdateRequest params);

    Boolean changePasswordByPersonal(PersonalPasswordUpdateRequest params);

    /**
     * 根据 userId 职工信息
     * @param userId 用户ID
     */
    EmployeeDetailResponse personalInfoByUserId(Long userId);

    EmployeeUpdateResponse updateRole(EmployeeUpdateRoleRequest params);

    Employee register(EmployeeSpecAddRequest params);

    Boolean changeInfoByPersonalV2(PersonalInfoSpecUpdateRequest params);

    Long categoryCount(List<Long> categoryIdList);
}

