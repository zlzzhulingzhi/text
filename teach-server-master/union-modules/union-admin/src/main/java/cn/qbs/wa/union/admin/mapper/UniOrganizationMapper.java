package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.UniOrganization;
import cn.qbs.wa.union.admin.pojo.uniorganization.UniOrganizationDetailResponse;
import cn.qbs.wa.union.admin.pojo.uniorganization.UniOrganizationPageRequest;
import cn.qbs.wa.union.admin.pojo.uniorganization.UniOrganizationPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * 统一机构(UniOrganization)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
public interface UniOrganizationMapper extends BaseMapper<UniOrganization> {

    IPage<UniOrganizationPageResponse> page(@Param("page") IPage<?> page, @Param("params") UniOrganizationPageRequest params);

    UniOrganizationDetailResponse selectDetailById(Serializable id);
    
}

