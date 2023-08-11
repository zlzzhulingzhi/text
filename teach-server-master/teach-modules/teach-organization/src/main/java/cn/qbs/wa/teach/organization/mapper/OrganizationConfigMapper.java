package cn.qbs.wa.teach.organization.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.organization.pojo.organizationconfig.OrganizationDecorationDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.organization.entity.OrganizationConfig;
import cn.qbs.wa.teach.organization.pojo.organizationconfig.OrganizationConfigDetailResponse;
import cn.qbs.wa.teach.organization.pojo.organizationconfig.OrganizationConfigPageRequest;
import cn.qbs.wa.teach.organization.pojo.organizationconfig.OrganizationConfigPageResponse;

/**
 * 机构配置表(OrganizationConfig)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 16:04:26
 */
public interface OrganizationConfigMapper extends BaseMapper<OrganizationConfig> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationConfig> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<OrganizationConfig> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<OrganizationConfig> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<OrganizationConfig> entities);

    IPage<OrganizationConfigPageResponse> page(@Param("page") IPage<?> page, @Param("params") OrganizationConfigPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    OrganizationConfigDetailResponse selectDetailById(@Param("id") Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    int insertByDefault(@Param("entity") OrganizationConfig organizationConfig);

    @InterceptorIgnore(tenantLine = "true")
    OrganizationDecorationDTO loginPage(@Param("orgId") Long orgId);
}

