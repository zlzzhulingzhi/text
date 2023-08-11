package cn.qbs.wa.teach.basic.service.inner;


import cn.qbs.wa.teach.basic.entity.Role;
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

