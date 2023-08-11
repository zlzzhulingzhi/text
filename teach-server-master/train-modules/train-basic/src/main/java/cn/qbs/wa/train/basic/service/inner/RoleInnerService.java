package cn.qbs.wa.train.basic.service.inner;

import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.pojo.role.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【系统角色】(Role)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface RoleInnerService extends IService<Role> {

    List<Long> getMenuIds();
}

