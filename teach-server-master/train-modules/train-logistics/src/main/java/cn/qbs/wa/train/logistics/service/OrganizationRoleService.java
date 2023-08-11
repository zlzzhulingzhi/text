package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.OrganizationRole;
import cn.qbs.wa.train.logistics.pojo.organizationrole.*;
import cn.qbs.wa.train.logistics.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【机构角色】(OrganizationRole)表服务接口
 *
 * @author makejava
 * @since 2021-11-10 08:42:56
 */
public interface OrganizationRoleService extends IService<OrganizationRole> {

    /**
     * 新增【机构角色】
     * @param params
     * @return
     */
    boolean add(OrganizationRoleAddRequest params);

    /**
     * 分页查询【机构角色】
     * @param params
     * @return
     */
    IPage<OrganizationRolePageResponse> page(OrganizationRolePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    OrganizationRoleDetailResponse detail(Long id);

    /**
     * 更新【机构角色】
     * @param params
     * @return
     */
    boolean update(OrganizationRoleUpdateRequest params);

    /**
     * 删除【机构角色】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<OrganizationRolePageResponse> listRole(OrganizationRoleListRequest params);

    void batchEnabled(Integer flag, List<Long> idList);

    List<OrgDeptOrRoleResponseDTO> getOrgRole(Serializable id);
}

