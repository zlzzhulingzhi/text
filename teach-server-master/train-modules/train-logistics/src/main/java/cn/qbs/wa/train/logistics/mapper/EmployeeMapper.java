package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Employee;
import cn.qbs.wa.train.logistics.pojo.employee.EmployeeDetailResponse;
import cn.qbs.wa.train.logistics.pojo.employee.EmployeeListRequest;
import cn.qbs.wa.train.logistics.pojo.employee.EmployeePageRequest;
import cn.qbs.wa.train.logistics.pojo.employee.EmployeePageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 职工(Employee)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-09 20:11:16
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Employee> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Employee> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Employee> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Employee> entities);

    @InterceptorIgnore(tenantLine = "true")
    IPage<EmployeePageResponse> page(@Param("page") IPage<?> page, @Param("params") EmployeePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    EmployeeDetailResponse selectDetailById(Serializable id);

    List<EmployeePageResponse> listEmployee(EmployeeListRequest params);

    List<EmployeePageResponse> listEmployeeWithRole(@Param("params") EmployeeListRequest request);

    @InterceptorIgnore(tenantLine = "true")
    List<EmployeePageResponse> listEmployeeRoleName(@Param("ids") List<Long> ids);

    @InterceptorIgnore(tenantLine = "true")
    String getNameById(Serializable id);
}

