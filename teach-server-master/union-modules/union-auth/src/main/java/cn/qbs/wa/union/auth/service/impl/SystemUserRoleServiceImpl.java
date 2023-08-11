package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemUserRole;
import cn.qbs.wa.union.auth.mapper.SystemUserRoleMapper;
import cn.qbs.wa.union.auth.service.SystemUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【平台用户角色关联关系】(SystemUserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@Slf4j
@Service("systemUserRoleService")
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements SystemUserRoleService {


}

