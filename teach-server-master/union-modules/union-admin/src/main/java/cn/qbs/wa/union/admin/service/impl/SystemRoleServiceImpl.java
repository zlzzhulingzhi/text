package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.entity.SystemRoleMenu;
import cn.qbs.wa.union.admin.entity.SystemUserRole;
import cn.qbs.wa.union.admin.mapper.SystemRoleMapper;
import cn.qbs.wa.union.admin.mapper.SystemUserRoleMapper;
import cn.qbs.wa.union.admin.pojo.systemrole.*;
import cn.qbs.wa.union.admin.service.SystemRoleMenuService;
import cn.qbs.wa.union.admin.service.SystemRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【系统角色】(SystemRole)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Slf4j
@Service("systemRoleService")
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService {

    @Autowired
    SystemRoleMenuService systemRoleMenuService;

    @Autowired
    SystemUserRoleMapper systemUserRoleMapper;

    @Override
    @Transactional
    public boolean add(SystemRoleAddRequest params) {
        SystemRole systemRole = new SystemRole();
        BeanUtils.copyProperties(params, systemRole);
        boolean save = this.save(systemRole);
        saveRoleMenu(params.getMenuIdList(), systemRole.getId());
        return save;
    }

    private void saveRoleMenu(List<Long> menuIdList, Long roleId) {
        systemRoleMenuService.remove(new LambdaQueryWrapper<SystemRoleMenu>().eq(SystemRoleMenu::getRoleId,roleId));
        if (CollUtil.isNotEmpty(menuIdList)) {
            List<SystemRoleMenu> systemRoleMenuList = menuIdList.stream().map(menuId -> {
                SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
                systemRoleMenu.setRoleId(roleId);
                systemRoleMenu.setMenuId(menuId);
                return systemRoleMenu;
            }).collect(Collectors.toList());
            systemRoleMenuService.saveBatch(systemRoleMenuList);
        }
    }

    @Override
    public IPage<SystemRolePageResponse> page(SystemRolePageRequest params) {
        // 不能选择比当前操作人员角色高的角色
        List<SystemRole> roles = systemUserRoleMapper.listRoleByUserId(SecurityContextHolder.getUserId());
        if (CollUtil.isNotEmpty(roles)) {
            Integer priority = roles.stream().map(SystemRole::getPriority).distinct().max(Comparator.naturalOrder()).get();
            params.setPriority(priority);
        }
        if (params.getPriority() == null) {
            params.setPriority(0);
        }
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemRoleDetailResponse detail(Serializable id) {
        SystemRoleDetailResponse systemRoleDetailResponse = baseMapper.selectDetailById(id);
        List<SystemRoleMenu> systemRoleMenuList = systemRoleMenuService.list(new LambdaQueryWrapper<SystemRoleMenu>().eq(SystemRoleMenu::getRoleId, id));
        if(CollUtil.isNotEmpty(systemRoleMenuList)){
            systemRoleDetailResponse.setMenuIdList(systemRoleMenuList.stream().map(SystemRoleMenu::getMenuId).collect(Collectors.toList()));
        }
        return systemRoleDetailResponse;
    }

    @Override
    public boolean update(SystemRoleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }

        SystemRole systemRole = new SystemRole();
        BeanUtils.copyProperties(params, systemRole);
        boolean update = this.updateById(systemRole);

        saveRoleMenu(params.getMenuIdList(), systemRole.getId());
        return  update;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        if(CollUtil.isNotEmpty(idList)){
            for (Long roleId : idList) {
                List<SystemUserRole> systemUserRoleList = systemUserRoleMapper.selectList(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getRoleId, roleId));
                if(CollUtil.isNotEmpty(systemUserRoleList)){
                    throw new ServiceException("该角色已被使用,无法删除");
                }
            }
        }
        return this.removeByIds(idList);
    }

}

