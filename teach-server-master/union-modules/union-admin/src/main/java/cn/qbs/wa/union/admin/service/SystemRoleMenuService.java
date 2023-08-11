package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemRoleMenu;
import cn.qbs.wa.union.admin.pojo.systemrolemenu.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
public interface SystemRoleMenuService extends IService<SystemRoleMenu> {

    /**
     * 新增【角色菜单关联关系】
     * @param params
     * @return
     */
    boolean add(SystemRoleMenuAddRequest params);

    /**
     * 分页查询【角色菜单关联关系】
     * @param params
     * @return
     */
    IPage<SystemRoleMenuPageResponse> page(SystemRoleMenuPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemRoleMenuDetailResponse detail(Serializable id);

    /**
     * 更新【角色菜单关联关系】
     * @param params
     * @return
     */
    boolean update(SystemRoleMenuUpdateRequest params);

    /**
     * 删除【角色菜单关联关系】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);


}

