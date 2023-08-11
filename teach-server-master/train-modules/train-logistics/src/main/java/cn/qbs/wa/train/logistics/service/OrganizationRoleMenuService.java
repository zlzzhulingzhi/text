package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.OrganizationRoleMenu;
import cn.qbs.wa.train.logistics.pojo.organizationrolemenu.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(OrganizationRoleMenu)表服务接口
 *
 * @author makejava
 * @since 2021-11-12 08:58:31
 */
public interface OrganizationRoleMenuService extends IService<OrganizationRoleMenu> {

    /**
     * 新增【角色菜单关联关系】
     * @param params
     * @return
     */
    boolean add(OrganizationRoleMenuAddRequest params);

    /**
     * 分页查询【角色菜单关联关系】
     * @param params
     * @return
     */
    IPage<OrganizationRoleMenuPageResponse> page(OrganizationRoleMenuPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    OrganizationRoleMenuDetailResponse detail(Serializable id);

    /**
     * 更新【角色菜单关联关系】
     * @param params
     * @return
     */
    boolean update(OrganizationRoleMenuUpdateRequest params);

    /**
     * 删除【角色菜单关联关系】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<OrganizationRoleMenu> listByAdmin(Long orgId);

    void deleteByAdminIds(List<Long> idList);
}

