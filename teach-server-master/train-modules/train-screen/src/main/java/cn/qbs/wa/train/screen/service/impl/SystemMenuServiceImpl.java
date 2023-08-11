package cn.qbs.wa.train.screen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.screen.entity.SystemMenu;
import cn.qbs.wa.train.screen.mapper.SystemMenuMapper;
import cn.qbs.wa.train.screen.pojo.permission.MenuPermissionResponse;
import cn.qbs.wa.train.screen.pojo.permission.SystemMenuTree;
import cn.qbs.wa.train.screen.service.SystemMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统菜单(SystemMenu)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 13:59:29
 */
@Slf4j
@Service("systemMenuService")
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService {

    @Override
    public MenuPermissionResponse getMenuPermission() {
        MenuPermissionResponse menuPermissionResponse = new MenuPermissionResponse();
        List<SystemMenu> menuList =  baseMapper.listMenu();
        if (CollUtil.isNotEmpty(menuList)) {
            List<SystemMenuTree> list = menuList.stream().map(e -> {
                SystemMenuTree systemMenuTree = BeanUtil.copyProperties(e, SystemMenuTree.class);
                systemMenuTree.setMenuName(e.getName());
                return systemMenuTree;
            }).collect(Collectors.toList());
            menuPermissionResponse.setMenuList(TreeUtil.tree(list));
        }
        return menuPermissionResponse;
    }
}

