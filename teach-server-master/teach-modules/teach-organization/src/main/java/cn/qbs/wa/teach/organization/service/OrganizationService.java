package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.Organization;
import cn.qbs.wa.teach.organization.pojo.dept.TreeDeptResponse;
import cn.qbs.wa.teach.organization.pojo.organization.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 组织机构(Organization)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
public interface OrganizationService extends IService<Organization> {

    /**
     * 新增组织机构
     *
     * @param params
     * @return
     */
    boolean add(OrganizationAddRequest params);

    /**
     * 分页查询组织机构
     *
     * @param params
     * @return
     */
    IPage<OrganizationPageResponse> page(OrganizationPageRequest params);

    /**
     * 商城分页查询组织机构
     *
     * @param params
     * @return
     */
    IPage<OrganizationPageResponse> shopGetOrgPage(OrganizationPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    OrganizationDetailResponse detail(Serializable id);

    @Transactional(rollbackFor = Exception.class)
    boolean adminUpdate(OrganizationUpdateRequest params);

    /**
     * 更新组织机构
     *
     * @param params
     * @return
     */
    boolean update(OrganizationUpdateRequest params);

    /**
     * 删除组织机构
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<OrganizationDeptResponse> orgDeptList();

    void batchEnabled(Integer flag, List<Long> idList);

    List<OrganizationSelectListResponse> listOrg();

    /**
     * 获取机构及部门树形列表
     * @return
     */
    List<TreeDeptResponse> orgDeptTreeList();

}

