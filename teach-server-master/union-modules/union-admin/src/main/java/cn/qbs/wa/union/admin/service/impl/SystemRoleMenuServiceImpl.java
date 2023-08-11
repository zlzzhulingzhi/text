package cn.qbs.wa.union.admin.service.impl;

import cn.qbs.wa.union.admin.entity.SystemRoleMenu;
import cn.qbs.wa.union.admin.mapper.SystemRoleMenuMapper;
import cn.qbs.wa.union.admin.pojo.systemrolemenu.*;
import cn.qbs.wa.union.admin.service.SystemRoleMenuService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Slf4j
@Service("systemRoleMenuService")
public class SystemRoleMenuServiceImpl extends ServiceImpl<SystemRoleMenuMapper, SystemRoleMenu> implements SystemRoleMenuService {

    @Override
    public boolean add(SystemRoleMenuAddRequest params) {
        SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
        BeanUtils.copyProperties(params, systemRoleMenu);
        return this.save(systemRoleMenu);
    }

    @Override
    public IPage<SystemRoleMenuPageResponse> page(SystemRoleMenuPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemRoleMenuDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemRoleMenuUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        SystemRoleMenu systemRoleMenu = new SystemRoleMenu();
        BeanUtils.copyProperties(params, systemRoleMenu);
        return this.updateById(systemRoleMenu);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }



}

