package cn.qbs.wa.train.basic.service.impl;

import cn.qbs.wa.train.basic.entity.RoleMenu;
import cn.qbs.wa.train.basic.mapper.RoleMenuMapper;
import cn.qbs.wa.train.basic.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

