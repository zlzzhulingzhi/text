package cn.qbs.wa.train.basic.service.impl.inner;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.basic.entity.Application;
import cn.qbs.wa.train.basic.entity.Menu;
import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.entity.RoleMenu;
import cn.qbs.wa.train.basic.mapper.MenuMapper;
import cn.qbs.wa.train.basic.mapper.RoleMapper;
import cn.qbs.wa.train.basic.mapper.RoleMenuMapper;
import cn.qbs.wa.train.basic.pojo.menu.TreeMenuResponse;
import cn.qbs.wa.train.basic.pojo.role.*;
import cn.qbs.wa.train.basic.pojo.rolemenu.RoleMenuVO;
import cn.qbs.wa.train.basic.service.ApplicationService;
import cn.qbs.wa.train.basic.service.RoleMenuService;
import cn.qbs.wa.train.basic.service.inner.RoleInnerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service("roleInnerService")
public class RoleInnerServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleInnerService {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    ApplicationService applicationService;

    @Override
    public List<Long> getMenuIds() {
        List<Long> menuIdList = new ArrayList<>();
        List<Application> applicationList = applicationService.list(new LambdaQueryWrapper<Application>().like(Application::getAppTypeId, Constants.ORG_APP_TYPE_ID));
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getEnabled, Constants.DB_TRUE).in(Menu::getAppId,applicationList.stream().map(Application::getId).collect(Collectors.toList())).orderByAsc(Menu::getAppId).orderByAsc(Menu::getSort));
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                menuIdList.add(menu.getId());
            }
        }
        return menuIdList;
    }

}

