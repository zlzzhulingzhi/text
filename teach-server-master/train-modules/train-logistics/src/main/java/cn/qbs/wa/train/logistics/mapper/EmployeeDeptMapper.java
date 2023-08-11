package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.EmployeeDept;
import cn.qbs.wa.train.logistics.pojo.employeedept.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 职工部门表(EmployeeDept)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-09 20:11:35
 */
public interface EmployeeDeptMapper extends BaseMapper<EmployeeDept> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<EmployeeDept> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<EmployeeDept> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<EmployeeDept> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<EmployeeDept> entities);

    IPage<EmployeeDeptPageResponse> page(@Param("page") IPage<?> page, @Param("params") EmployeeDeptPageRequest params);

    EmployeeDeptDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<Long> listIdByEmployeeId(Serializable id);

    /**
     * 查询部门员工列表
     * @param params 查询条件
     * @return 部门职工列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<EmployeeDeptTreeListResponse> list(@Param("params") EmployeeDeptListRequest params);
}

