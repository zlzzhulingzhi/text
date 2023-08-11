package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qbs.wa.teach.basic.entity.UserRole;

import java.util.List;

/**
 * 【平台用户角色关联关系】(UserRole)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 15:48:21
 */
public interface UserRoleService extends IService<UserRole> {

    List<Role> listRoleByUserId(Long currUserId);

    List<Role> filterInvalid(List<Role> list);
}

