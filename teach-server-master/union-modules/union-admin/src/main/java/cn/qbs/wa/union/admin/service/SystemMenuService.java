package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemMenu;
import cn.qbs.wa.union.admin.pojo.systemmenu.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统菜单】(SystemMenu)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:04
 */
public interface SystemMenuService extends IService<SystemMenu> {

    /**
     * 新增【系统菜单】
     * @param params
     * @return
     */
    boolean add(SystemMenuAddRequest params);

    /**
     * 分页查询【系统菜单】
     * @param params
     * @return
     */
    IPage<SystemMenuPageResponse> page(SystemMenuPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemMenuDetailResponse detail(Serializable id);

    /**
     * 更新【系统菜单】
     * @param params
     * @return
     */
    boolean update(SystemMenuUpdateRequest params);

    /**
     * 删除【系统菜单】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<SystemMenuDetailResponse> menuList();

    List<SystemMenuTreeResponse> getTreeList(SystemMenuTreeRequest request);

    void batchEnabled(Integer flag, List<Long> idList);
}

