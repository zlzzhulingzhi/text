package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.union.admin.entity.SystemMenu;
import cn.qbs.wa.union.admin.mapper.SystemMenuMapper;
import cn.qbs.wa.union.admin.pojo.systemmenu.*;
import cn.qbs.wa.union.admin.service.SystemMenuService;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 【系统菜单】(SystemMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:04
 */
@Slf4j
@Service("systemMenuService")
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService {

    @Override
    public boolean add(SystemMenuAddRequest params) {
        List<SystemMenu> list = list(new LambdaQueryWrapper<SystemMenu>().eq(SystemMenu::getPermission, params.getPermission()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("该权限码已存在,请重新修改");
        }
        SystemMenu systemMenu = new SystemMenu();
        BeanUtils.copyProperties(params, systemMenu);
        return this.save(systemMenu);
    }

    @Override
    public IPage<SystemMenuPageResponse> page(SystemMenuPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemMenuDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemMenuUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        SystemMenu systemMenu = new SystemMenu();
        BeanUtils.copyProperties(params, systemMenu);
        return this.updateById(systemMenu);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<SystemMenuDetailResponse> menuList() {
        List<SystemMenu> systemMenuList = list(new LambdaQueryWrapper<SystemMenu>().eq(SystemMenu::getEnabled, Constants.DB_TRUE).orderByAsc(SystemMenu::getSort));
        return BeanUtil.copyToList(systemMenuList, SystemMenuDetailResponse.class);
    }

    @Override
    public List<SystemMenuTreeResponse> getTreeList(SystemMenuTreeRequest request) {
        List<SystemMenu> systemMenuList = list(new LambdaQueryWrapper<SystemMenu>()
                .eq(request.getParentId() != null, SystemMenu::getParentId, request.getParentId())
                .eq(request.getEnabled() != null, SystemMenu::getEnabled, request.getEnabled()).orderByAsc(SystemMenu::getSort));
        if (CollUtil.isNotEmpty(systemMenuList)) {
            return TreeUtil.tree(BeanUtil.copyToList(systemMenuList, SystemMenuTreeResponse.class));
        }
        return null;
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<SystemMenu> menus = new ArrayList<>();
        for (Long roleId : idList) {
            SystemMenu menu = new SystemMenu();
            menu.setId(roleId);
            menu.setEnabled(flag);
            menus.add(menu);
        }
        updateBatchById(menus);
    }

}

