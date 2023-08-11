package cn.qbs.wa.union.auth.service.impl;

import cn.qbs.wa.union.auth.entity.SystemMenu;
import cn.qbs.wa.union.auth.mapper.SystemMenuMapper;
import cn.qbs.wa.union.auth.service.SystemMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 【系统菜单】(SystemMenu)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:04
 */
@Slf4j
@Service("systemMenuService")
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService {


}

