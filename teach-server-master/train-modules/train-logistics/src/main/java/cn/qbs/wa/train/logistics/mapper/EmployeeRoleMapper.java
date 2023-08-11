package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.EmployeeRole;
import cn.qbs.wa.train.logistics.entity.OrganizationRole;
import cn.qbs.wa.train.logistics.pojo.employeerole.EmployeeRoleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.employeerole.EmployeeRolePageRequest;
import cn.qbs.wa.train.logistics.pojo.employeerole.EmployeeRolePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【职工角色关联关系】(EmployeeRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-09 20:12:51
 */
public interface EmployeeRoleMapper extends BaseMapper<EmployeeRole> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<EmployeeRole> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<EmployeeRole> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<EmployeeRole> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<EmployeeRole> entities);

    IPage<EmployeeRolePageResponse> page(@Param("page") IPage<?> page, @Param("params") EmployeeRolePageRequest params);

    EmployeeRoleDetailResponse selectDetailById(Serializable id);

    /**
     * 删除【职工角色关联关系】
     * @param employeeId
     * @return
     */
    void deleteByEmployeeIdAndRoleId(@Param("employeeId") Long employeeId, @Param("roleId") Long roleId);

    List<OrganizationRole> getRole(Long employeeId);
}

