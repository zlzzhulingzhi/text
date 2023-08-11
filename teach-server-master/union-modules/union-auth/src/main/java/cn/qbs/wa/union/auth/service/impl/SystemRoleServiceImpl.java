package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemRole;
import cn.qbs.wa.union.auth.mapper.SystemRoleMapper;
import cn.qbs.wa.union.auth.service.SystemRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【系统角色】(SystemRole)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Slf4j
@Service("systemRoleService")
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService {

}

