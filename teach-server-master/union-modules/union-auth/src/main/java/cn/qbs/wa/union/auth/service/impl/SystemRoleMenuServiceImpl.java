package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemRoleMenu;
import cn.qbs.wa.union.auth.mapper.SystemRoleMenuMapper;
import cn.qbs.wa.union.auth.service.SystemRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Slf4j
@Service("systemRoleMenuService")
public class SystemRoleMenuServiceImpl extends ServiceImpl<SystemRoleMenuMapper, SystemRoleMenu> implements SystemRoleMenuService {


}

