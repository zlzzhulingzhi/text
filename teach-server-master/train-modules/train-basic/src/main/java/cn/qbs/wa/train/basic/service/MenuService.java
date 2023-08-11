package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.Menu;
import cn.qbs.wa.train.basic.pojo.menu.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统菜单】(Menu)表服务接口
 *
 * @author makejava
 * @since 2021-11-04 16:28:07
 */
public interface MenuService extends IService<Menu> {

    /**
     * 新增【系统菜单】
     * @param params
     * @return
     */
    boolean add(MenuAddRequest params);

    /**
     * 分页查询【系统菜单】
     * @param params
     * @return
     */
    IPage<MenuPageResponse> page(MenuPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    MenuDetailResponse detail(Serializable id);

    /**
     * 更新【系统菜单】
     * @param params
     * @return
     */
    boolean update(MenuUpdateRequest params);

    /**
     * 删除【系统菜单】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    TreeMenuTotalResponse getTreeMenuTotal(TreeMenuTotalRequest request);

    List<Menu> listMenu(MenuListRequest params);

    void batchEnabled(Integer flag, List<Long> idList);

    List<Menu> listMenuByAppType(Long appTypeId);
}

