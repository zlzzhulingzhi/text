package cn.qbs.wa.teach.basic.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qbs.wa.teach.basic.mapper.RoleMenuMapper;
import cn.qbs.wa.teach.basic.entity.RoleMenu;
import cn.qbs.wa.teach.basic.service.RoleMenuService;

/**
 * 【角色菜单关联关系】(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@Slf4j
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

