package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.entity.SystemUserRole;
import cn.qbs.wa.union.admin.mapper.SystemUserRoleMapper;
import cn.qbs.wa.union.admin.pojo.systemuserrole.*;
import cn.qbs.wa.union.admin.service.SystemUserRoleService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * 【平台用户角色关联关系】(SystemUserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@Slf4j
@Service("systemUserRoleService")
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements SystemUserRoleService {

    @Override
    public boolean add(SystemUserRoleAddRequest params) {
        SystemUserRole systemUserRole = new SystemUserRole();
        BeanUtils.copyProperties(params, systemUserRole);
        return this.save(systemUserRole);
    }

    @Override
    public IPage<SystemUserRolePageResponse> page(SystemUserRolePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemUserRoleDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemUserRoleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        SystemUserRole systemUserRole = new SystemUserRole();
        BeanUtils.copyProperties(params, systemUserRole);
        return this.updateById(systemUserRole);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<SystemRole> listRoleByUserId(Long userId) {
        return this.baseMapper.listRoleByUserId(userId);
    }

    @Override
    public List<SystemRole> filterInvalid(List<SystemRole> list) {
        if (CollUtil.isNotEmpty(list)) {
            // 不能选择比当前操作人员角色高的角色
            List<SystemRole> roles = this.listRoleByUserId(SecurityContextHolder.getUserId());
            if (CollUtil.isNotEmpty(roles)) {
                Integer priority = roles.stream().map(SystemRole::getPriority).distinct().max(Comparator.naturalOrder()).get();
                list.removeIf(e -> e.getPriority().compareTo(priority) > 0);
            }
        }
        return list;
    }
}

