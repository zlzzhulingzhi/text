package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Dept;
import cn.qbs.wa.train.logistics.pojo.dept.*;
import cn.qbs.wa.train.logistics.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 部门表(Dept)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-10 09:54:50
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Dept> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Dept> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Dept> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Dept> entities);

    IPage<DeptPageResponse> page(@Param("page") IPage<?> page, @Param("params") DeptPageRequest params);

    DeptDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<DeptListResponse> listDept(@Param("orgId") Long orgId,@Param("params") DeptListRequest deptListRequest);

    @InterceptorIgnore(tenantLine = "true")
    Integer getPeopleCountById(Long id);

    @InterceptorIgnore(tenantLine = "true")
    void updateDeptCount(Dept dept);

    List<OrgDeptOrRoleResponseDTO> getOrgDept(Serializable id);

    List<DeptListResponse> selectDeptList(@Param("params")DeptChildRequest params);

    Long selectDeptEmployeeCount(@Param("orgId") Long orgId, @Param("deptIdList") List<Long> deptIdList);

    @InterceptorIgnore(tenantLine = "true")
    Long selectDeptStudentCount(@Param("orgId") Long orgId, @Param("deptIdList") List<Long> deptIdList);

}

