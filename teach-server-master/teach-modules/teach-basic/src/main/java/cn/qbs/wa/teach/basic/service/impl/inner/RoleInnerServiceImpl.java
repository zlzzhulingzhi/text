package cn.qbs.wa.teach.basic.service.impl.inner;

import cn.qbs.wa.teach.basic.entity.Application;
import cn.qbs.wa.teach.basic.entity.Menu;
import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.basic.mapper.MenuMapper;
import cn.qbs.wa.teach.basic.mapper.RoleMapper;
import cn.qbs.wa.teach.basic.service.ApplicationService;
import cn.qbs.wa.teach.basic.service.inner.RoleInnerService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

