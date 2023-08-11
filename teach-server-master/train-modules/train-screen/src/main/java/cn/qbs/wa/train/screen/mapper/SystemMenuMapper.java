package cn.qbs.wa.train.screen.mapper;

import cn.qbs.wa.train.screen.entity.SystemMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 系统菜单(SystemMenu)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 13:59:29
 */
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    List<SystemMenu> listMenu();

}

