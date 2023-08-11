package cn.qbs.wa.teach.organization.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.organization.entity.EmployeeInfo;
import cn.qbs.wa.teach.organization.pojo.employeeinfo.EmployeeInfoDetailResponse;
import cn.qbs.wa.teach.organization.pojo.employeeinfo.EmployeeInfoPageRequest;
import cn.qbs.wa.teach.organization.pojo.employeeinfo.EmployeeInfoPageResponse;

/**
 * 职工信息(EmployeeInfo)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-01-21 11:30:03
 */
public interface EmployeeInfoMapper extends BaseMapper<EmployeeInfo> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<EmployeeInfo> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<EmployeeInfo> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<EmployeeInfo> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<EmployeeInfo> entities);

    IPage<EmployeeInfoPageResponse> page(@Param("page") IPage<?> page, @Param("params") EmployeeInfoPageRequest params);

    EmployeeInfoDetailResponse selectDetailById(Serializable id);
    
}

