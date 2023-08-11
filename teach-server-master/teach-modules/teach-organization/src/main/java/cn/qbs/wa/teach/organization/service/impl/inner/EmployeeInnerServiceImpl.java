package cn.qbs.wa.teach.organization.service.impl.inner;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserAddDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserInnerAddResultDTO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.EmployeeDept;
import cn.qbs.wa.teach.organization.entity.EmployeeRole;
import cn.qbs.wa.teach.organization.enums.InitRoleEnum;
import cn.qbs.wa.teach.organization.enums.SexEnum;
import cn.qbs.wa.teach.organization.mapper.EmployeeMapper;
import cn.qbs.wa.teach.organization.mapper.OrganizationRoleMapper;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeAddRequest;
import cn.qbs.wa.teach.organization.pojo.organization.inner.UpdateBindUserRequest;
import cn.qbs.wa.teach.organization.service.EmployeeDeptService;
import cn.qbs.wa.teach.organization.service.EmployeeRoleService;
import cn.qbs.wa.teach.organization.service.impl.AsyncServiceImpl;
import cn.qbs.wa.teach.organization.service.inner.EmployeeInnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 职工(Employee)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:11:19
 */
@Slf4j
@Service("employeeInnerService")
public class EmployeeInnerServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeInnerService {

    @Autowired
    RemoteUserService remoteUserService;

    @Autowired
    EmployeeRoleService employeeRoleService;

    @Autowired
    EmployeeDeptService employeeDeptService;

    @Autowired
    OrganizationRoleMapper organizationRoleMapper;

    @Autowired
    private AsyncServiceImpl asyncService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee add(EmployeeAddRequest params) {
        params.setSex(SexEnum.OTHER.getSex());
        UserAddDTO userAddDTO = new UserAddDTO();
        BeanUtils.copyProperties(params, userAddDTO);
        R<UserInnerAddResultDTO> userResult = remoteUserService.innerAdd(userAddDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        //职工新增
        Employee employee = new Employee();
        BeanUtils.copyProperties(params, employee);
        Long count = this.lambdaQuery().eq(Employee::getOrgId, params.getOrgId()).eq(Employee::getUserId, employee.getUserId()).count();
        if (count != null && count > 0) {
            throw new ServiceException(String.format("手机号码 %s 已存在", AesUtil.decode(employee.getPhone())));
        }
        this.save(employee);

        //职工部门新增
        saveEmployeeDept(params.getDeptIdList(), employee);
        //职工新增机构管理员角色
        Long roleId = organizationRoleMapper.getEmployeeRole(employee.getOrgId(), InitRoleEnum.ORG_MASTER.getCode());
        saveEmployeeRole(Collections.singletonList(roleId), employee);
        return employee;
    }


    private void saveEmployeeRole(List<Long> roleIdList, Employee employee) {
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            List<EmployeeRole> employeeRoles = new ArrayList<>();
            for (Long roleId : roleIdList) {
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEmployeeId(employee.getId());
                employeeRole.setRoleId(roleId);
                employeeRoles.add(employeeRole);
            }
            employeeRoleService.saveBatch(employeeRoles);
        }
    }

    private void saveEmployeeDept(List<Long> deptIdList, Employee employee) {
        if (CollectionUtils.isNotEmpty(deptIdList)) {
            List<EmployeeDept> employeeDeptList = new ArrayList<>();
            for (Long deptId : deptIdList) {
                if (StringUtils.isNotBlank(deptId.toString())) {
                    EmployeeDept employeeDept = new EmployeeDept();
                    employeeDept.setEmployeeId(employee.getId());
                    employeeDept.setDeptId(deptId);
                    employeeDeptList.add(employeeDept);
                }
            }
            employeeDeptService.saveBatch(employeeDeptList);
        }
    }

    @Override
    public Boolean updateBindUser(UpdateBindUserRequest params) {
        Long orgId = params.getOrgId();
        if (params.getOldUserId() != null) {
            // 创建新用户并绑定为超级管理员
            // 获取当前机构的超级管理员角色ID
            Long roleId = organizationRoleMapper.getEmployeeRole(orgId, InitRoleEnum.ORG_MASTER.getCode());
            Employee employee = this.lambdaQuery().eq(Employee::getOrgId, orgId).eq(Employee::getUserId, params.getUserId()).one();
            if (employee == null) {
                UserAddDTO userAddDTO = BeanUtil.copyProperties(params, UserAddDTO.class);
                userAddDTO.setSex(SexEnum.OTHER.getSex());
                R<UserInnerAddResultDTO> userResult = remoteUserService.innerAdd(userAddDTO);
                if (R.FAIL == userResult.getCode()) {
                    throw new ServiceException(userResult.getMsg());
                }
                // 职工新增
                employee = BeanUtil.copyProperties(params, Employee.class);
                employee.setSex(SexEnum.OTHER.getSex());
                this.save(employee);
            } else {
                if (Constants.DB_FAIL.equals(employee.getEnabled())) {
                    // 用户被禁用的情况下，更新为 启用 状态
                    Employee updater = new Employee();
                    updater.setId(employee.getId());
                    updater.setEnabled(Constants.DB_TRUE);
                    this.updateById(updater);
                }
            }
            // 绑定为超级管理员
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setEmployeeId(employee.getId());
            employeeRole.setRoleId(roleId);
            employeeRoleService.save(employeeRole);

            // 旧的用户账号取消管理员绑定并禁用账号
            Employee emp = this.lambdaQuery().eq(Employee::getOrgId, orgId).eq(Employee::getUserId, params.getOldUserId()).one();
            if (emp != null) {
                employeeRoleService.deleteByEmployeeIdAndRoleId(emp.getId(), roleId);
                Employee updater = new Employee();
                updater.setId(emp.getId());
                updater.setEnabled(Constants.DB_FAIL);
                this.updateById(updater);
                // 职工禁用，退出登录
                asyncService.loggingOff(AsyncServiceImpl.LogOutType.EMPLOYEE, emp.getId());
            }
        } else {
            // 仅修改用户信息
            Employee emp = this.lambdaQuery().select(Employee::getId).eq(Employee::getOrgId, orgId).eq(Employee::getUserId, params.getUserId()).one();
            if (emp != null) {
                emp.setRealName(params.getRealName());
                this.updateById(emp);
            }
            UserAddDTO userAddDTO = new UserAddDTO();
            userAddDTO.setUserId(params.getUserId());
            userAddDTO.setRealName(params.getRealName());
            userAddDTO.setEmail(params.getEmail());
            R<UserInnerAddResultDTO> r = remoteUserService.innerAdd(userAddDTO);
            if (!r.isOk()) {
                log.error(r.getMsg());
            }
        }
        return true;
    }
}

