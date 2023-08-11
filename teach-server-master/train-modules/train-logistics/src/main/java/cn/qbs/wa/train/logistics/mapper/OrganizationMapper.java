package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.organization.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 组织机构(Organization)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    IPage<OrganizationPageResponse> page(@Param("page") IPage<?> page, @Param("params") OrganizationPageRequest params);

    Long getIdByOrgName(@Param("params") OrganizationPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<OrganizationPageResponse> shopGetOrgPage(@Param("page") IPage<?> page, @Param("params") OrganizationPageRequest params);

    OrganizationDetailResponse selectDetailById(Serializable id);


    List<OrganizationDeptResponse> getOrgDeptList();

    List<OrganizationSelectListResponse> listOrg();
}

