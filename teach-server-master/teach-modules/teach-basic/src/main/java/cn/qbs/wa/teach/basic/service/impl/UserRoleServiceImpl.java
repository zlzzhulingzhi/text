package cn.qbs.wa.teach.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qbs.wa.teach.basic.mapper.UserRoleMapper;
import cn.qbs.wa.teach.basic.entity.UserRole;
import cn.qbs.wa.teach.basic.service.UserRoleService;

import java.util.Comparator;
import java.util.List;

/**
 * 【平台用户角色关联关系】(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 15:48:21
 */
@Slf4j
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<Role> listRoleByUserId(Long userId) {
        return this.baseMapper.listRoleByUserId(userId);
    }

    @Override
    public List<Role> filterInvalid(List<Role> list) {
        if (CollUtil.isNotEmpty(list)) {
            // 不能选择比当前操作人员角色高的角色
            List<Role> roles = this.listRoleByUserId(SecurityContextHolder.getUserId());
            if (CollUtil.isNotEmpty(roles)) {
                Integer priority = roles.stream().map(Role::getPriority).distinct().max(Comparator.naturalOrder()).get();
                list.removeIf(e -> e.getPriority().compareTo(priority) > 0);
            }
        }
        return list;
    }
}

