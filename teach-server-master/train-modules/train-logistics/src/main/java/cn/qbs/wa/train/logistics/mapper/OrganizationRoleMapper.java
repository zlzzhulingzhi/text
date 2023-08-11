package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.OrganizationRole;
import cn.qbs.wa.train.logistics.pojo.organizationrole.OrganizationRoleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.organizationrole.OrganizationRoleListRequest;
import cn.qbs.wa.train.logistics.pojo.organizationrole.OrganizationRolePageRequest;
import cn.qbs.wa.train.logistics.pojo.organizationrole.OrganizationRolePageResponse;
import cn.qbs.wa.train.logistics.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【机构角色】(OrganizationRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-10 08:42:55
 */
public interface OrganizationRoleMapper extends BaseMapper<OrganizationRole> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationRole> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<OrganizationRole> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationRole> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<OrganizationRole> entities);

    IPage<OrganizationRolePageResponse> page(@Param("page") IPage<?> page, @Param("params") OrganizationRolePageRequest params);

    OrganizationRoleDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<OrganizationRole> listRole(OrganizationRoleListRequest params);

    List<OrgDeptOrRoleResponseDTO> getOrgRole(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    Long getEmployeeRole(Serializable id, String code);
}

