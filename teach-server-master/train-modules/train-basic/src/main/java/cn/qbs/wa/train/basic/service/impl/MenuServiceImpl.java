package cn.qbs.wa.train.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.train.basic.entity.Menu;
import cn.qbs.wa.train.basic.mapper.MenuMapper;
import cn.qbs.wa.train.basic.pojo.menu.*;
import cn.qbs.wa.train.basic.service.MenuService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【系统菜单】(Menu)表服务实现类
 *
 * @author makejava
 * @since 2021-11-04 16:28:07
 */
@Slf4j
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public boolean add(MenuAddRequest params) {
        List<Menu> list = list(new LambdaQueryWrapper<Menu>().eq(Menu::getPermission, params.getPermission()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("该权限码已存在,请重新修改");
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(params, menu);
        return this.save(menu);
    }

    @Override
    public IPage<MenuPageResponse> page(MenuPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public MenuDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    @Transactional
    public boolean update(MenuUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        List<Menu> list = list(new LambdaQueryWrapper<Menu>().eq(Menu::getPermission, params.getPermission()).ne(Menu::getId, params.getId()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("该权限码已存在,请重新修改");
        }
        Menu existMenu = getById(params.getId());
        Menu menu = new Menu();
        BeanUtils.copyProperties(params, menu);
        boolean updateFlag = this.updateById(menu);
        if (!existMenu.getAppId().equals(params.getAppId())) {
            updateChildrenMenu(params.getId(), params.getAppId());
        }
        return updateFlag;
    }

    private void updateChildrenMenu(Long parentId, Long appId) {
        List<Menu> childrenMenu = list(new LambdaQueryWrapper<Menu>().select(Menu::getId).eq(Menu::getParentId, parentId));
        if (CollUtil.isNotEmpty(childrenMenu)) {
            childrenMenu.forEach(i -> i.setAppId(appId));
            updateBatchById(childrenMenu);
            childrenMenu.forEach(i -> updateChildrenMenu(i.getId(), appId));
        }
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public TreeMenuTotalResponse getTreeMenuTotal(TreeMenuTotalRequest request) {
        TreeMenuTotalResponse treeMenuTotalResponse = new TreeMenuTotalResponse();
        List<Menu> menuList = list(new LambdaQueryWrapper<Menu>().eq(Menu::getEnabled, Constants.DB_TRUE).orderByAsc(Menu::getSort));
        if (CollectionUtils.isNotEmpty(menuList)) {
            List<TreeMenuResponse> treeMenu = new ArrayList<>();
            for (Menu menu : menuList) {
                TreeMenuResponse treeMenuResponse = new TreeMenuResponse();
                BeanUtils.copyProperties(menu, treeMenuResponse);
                treeMenuResponse.setMenuName(menu.getName());
                treeMenu.add(treeMenuResponse);
            }
            treeMenuTotalResponse.setMenu((List<TreeMenuResponse>) TreeUtil.tree(treeMenu));
        }
        return treeMenuTotalResponse;
    }

    @Override
    public List<Menu> listMenu(MenuListRequest params) {
        return list(new LambdaQueryWrapper<Menu>().eq(Menu::getEnabled, params.getEnabled()).in(Menu::getId, params.getMenuIdList()).orderByAsc(Menu::getAppId).orderByAsc(Menu::getSort));
    }

    @Override
    public List<Menu> listMenuByAppType(Long appTypeId) {
        return baseMapper.listMenuByAppType(appTypeId);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Menu> menus = new ArrayList<>();
        for (Long roleId : idList) {
            Menu menu = new Menu();
            menu.setId(roleId);
            menu.setEnabled(flag);
            menus.add(menu);
        }
        updateBatchById(menus);
    }

}

