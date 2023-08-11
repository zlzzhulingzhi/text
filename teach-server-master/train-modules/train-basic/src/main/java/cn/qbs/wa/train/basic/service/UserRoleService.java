package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【平台用户角色关联关系】(UserRole)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 15:48:21
 */
public interface UserRoleService extends IService<UserRole> {

    List<Role> listRoleByUserId(Long currUserId);

    /**
     * 过滤不合法的角色ID
     * @param list
     * @return
     */
    List<Role> filterInvalid(List<Role> list);
}

