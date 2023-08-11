package cn.qbs.wa.teach.organization.service.inner;

import cn.qbs.wa.teach.organization.entity.Organization;
import cn.qbs.wa.teach.organization.pojo.organization.OrganizationDetailResponse;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationInnerAddRequest;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationInnerUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;


/**
 * 组织机构(Organization)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
public interface OrganizationInnerService extends IService<Organization> {

    Long init(OrganizationInnerAddRequest params);

    boolean update(OrganizationInnerUpdateRequest params);

    OrganizationDetailResponse detail(Serializable id);

}

