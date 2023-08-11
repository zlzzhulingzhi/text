package cn.qbs.wa.teach.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.basic.entity.*;
import cn.qbs.wa.teach.basic.mapper.ApplicationTypeMapper;
import cn.qbs.wa.teach.basic.mapper.MenuMapper;

import cn.qbs.wa.teach.basic.mapper.RoleMapper;
import cn.qbs.wa.teach.basic.mapper.RoleMenuMapper;
import cn.qbs.wa.teach.basic.pojo.menu.TreeMenuResponse;

import cn.qbs.wa.teach.basic.pojo.role.*;
import cn.qbs.wa.teach.basic.pojo.rolemenu.RoleMenuVO;
import cn.qbs.wa.teach.basic.service.ApplicationService;
import cn.qbs.wa.teach.basic.service.RoleMenuService;
import cn.qbs.wa.teach.basic.service.RoleService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【系统角色】(Role)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@Slf4j
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    ApplicationService applicationService;

    @Resource
    ApplicationTypeMapper applicationTypeMapper;

    @Override
    public RoleOneResponse getRole(Long id) {
        Role existRole = getById(id);
        if (existRole == null) {
            throw new ServiceException("无法找到该角色");
        }
        RoleOneResponse roleResponse = new RoleOneResponse();
        BeanUtils.copyProperties(existRole, roleResponse);
        List<RoleMenuVO> menuList = roleMenuMapper.getFullRoleMenuByRoleIdList(Arrays.asList(id));
        if (CollectionUtils.isNotEmpty(menuList)) {
            List<TreeMenuResponse> treeMenu = new ArrayList<>();
            for (RoleMenuVO roleMenuVO : menuList) {
                TreeMenuResponse treeMenuResponse = new TreeMenuResponse();
                BeanUtils.copyProperties(roleMenuVO, treeMenuResponse);
                treeMenu.add(treeMenuResponse);
            }
            roleResponse.setMenu((List<TreeMenuResponse>) TreeUtil.tree(treeMenu));
        }
        return roleResponse;
    }

    @Override
    public RolePermissionResponse getRoleAdminPermission() {
        RolePermissionResponse response = new RolePermissionResponse();
        // 特殊处理：将平台应用分类 下的子分类查询出来。由于平台端需求，原本机构中心菜单映射成多个应用菜单，但是菜单配置却在一处配置，需要统一查询出来。
        List<ApplicationType> applicationTypeList = applicationTypeMapper.selectList(Wrappers.<ApplicationType>lambdaQuery().select(ApplicationType::getId).eq(ApplicationType::getParentId, Constants.PLAT_APP_TYPE_ID));
        List<Long> ids = applicationTypeList.isEmpty() ? new ArrayList<>(1) : applicationTypeList.stream().map(ApplicationType::getId).collect(Collectors.toList());
        ids.add(Constants.PLAT_APP_TYPE_ID);
        List<Application> applicationList = applicationService.list(new LambdaQueryWrapper<Application>().in(Application::getAppTypeId, ids));
        if (CollUtil.isNotEmpty(applicationList)) {
            List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getEnabled, Constants.DB_TRUE).in(Menu::getAppId, applicationList.stream().map(Application::getId).collect(Collectors.toList())).orderByAsc(Menu::getAppId).orderByAsc(Menu::getSort));
            if (CollectionUtils.isNotEmpty(menuList)) {
                List<TreeMenuResponse> menuTreeList = new ArrayList<>();
                for (Menu menu : menuList) {
                    TreeMenuResponse treeMenuResponse = new TreeMenuResponse();
                    BeanUtils.copyProperties(menu, treeMenuResponse);
                    treeMenuResponse.setMenuName(menu.getName());
                    menuTreeList.add(treeMenuResponse);
                }
                response.setMenu((List<TreeMenuResponse>) TreeUtil.tree(menuTreeList));
            }
        }
        return response;
    }

    @Override
    public IPage<RolePageResponse> pageRole(RolePageRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        Page<Role> page = page(new Page<>(request.getCurrent(), request.getSize()), new QueryWrapper<>());
        return baseMapper.pageRole(page, request);
    }

    @Override
    public void updateAdminRole(RoleUpdateRequest request) {
        Long orgId = SecurityContextHolder.getOrgId();
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        updateById(role);
        updateRoleMenu(request.getMenuIdList(), role);
    }

    @Override
    public void addAdminRole(RoleAddRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        save(role);
        addRoleMenu(request.getMenuIdList(), role);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Role> roles = new ArrayList<>();
        for (Long roleId : idList) {
            Role role = new Role();
            role.setId(roleId);
            role.setEnabled(flag);
            roles.add(role);
        }
        updateBatchById(roles);
    }

    @Override
    public RolePermissionResponse getRoleOrgPermission() {
        RolePermissionResponse response = new RolePermissionResponse();
        List<Application> applicationList = applicationService.list(new LambdaQueryWrapper<Application>().like(Application::getAppTypeId, Constants.ORG_APP_TYPE_ID));
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getEnabled, Constants.DB_TRUE).in(Menu::getAppId,applicationList.stream().map(Application::getId).collect(Collectors.toList())).orderByAsc(Menu::getAppId).orderByAsc(Menu::getSort));
        if (CollectionUtils.isNotEmpty(menuList)) {
            List<TreeMenuResponse> menuTreeList = new ArrayList<>();
            for (Menu menu : menuList) {
                TreeMenuResponse treeMenuResponse = new TreeMenuResponse();
                BeanUtils.copyProperties(menu, treeMenuResponse);
                treeMenuResponse.setMenuName(menu.getName());
                menuTreeList.add(treeMenuResponse);
            }
            response.setMenu((List<TreeMenuResponse>) TreeUtil.tree(menuTreeList));
        }
        return response;
    }

    private void updateRoleMenu(List<Long> menuIdList, Role role) {
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getId()));
        addRoleMenu(menuIdList, role);
    }


    private void addRoleMenu(List<Long> menuIdList, Role role) {
        if (CollectionUtils.isNotEmpty(menuIdList)) {
            List<RoleMenu> roleMenus = new ArrayList<>();
            for (Long menuId : menuIdList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(menuId);
                roleMenu.setRoleId(role.getId());
                roleMenus.add(roleMenu);
            }
            roleMenuService.saveBatch(roleMenus);
        }

    }
}

