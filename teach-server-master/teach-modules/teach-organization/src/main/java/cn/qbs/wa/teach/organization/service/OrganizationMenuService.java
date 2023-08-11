package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.OrganizationMenu;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(OrganizationMenu)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:26:19
 */
public interface OrganizationMenuService extends IService<OrganizationMenu> {

    /**
     * 新增【角色菜单关联关系】
     * @param params
     * @return
     */
    boolean add(OrganizationMenuAddRequest params);

    /**
     * 分页查询【角色菜单关联关系】
     * @param params
     * @return
     */
    IPage<OrganizationMenuPageResponse> page(OrganizationMenuPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    OrganizationMenuDetailResponse detail(Serializable id);

    /**
     * 更新【角色菜单关联关系】
     * @param params
     * @return
     */
    boolean update(OrganizationMenuUpdateRequest params);

    /**
     * 删除【角色菜单关联关系】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);


    List<OrganizationMenuListResponse> listOrganizationMenu(OrganizationMenuListRequest params);

    List<OrganizationMenu> adminListByOrgId(Serializable id);
}

