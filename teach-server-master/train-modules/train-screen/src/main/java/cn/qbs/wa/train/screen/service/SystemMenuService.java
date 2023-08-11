package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.SystemMenu;
import cn.qbs.wa.train.screen.pojo.permission.MenuPermissionResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统菜单(SystemMenu)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 13:59:29
 */
public interface SystemMenuService extends IService<SystemMenu> {

    /**
     * 获取菜单权限
     * @return
     */
    MenuPermissionResponse getMenuPermission();
}

