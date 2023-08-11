package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.basic.pojo.role.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【系统角色】(Role)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface RoleService extends IService<Role> {


    RoleOneResponse getRole(Long id);

    RolePermissionResponse getRoleAdminPermission();

    IPage<RolePageResponse> pageRole(RolePageRequest request);

    void updateAdminRole(RoleUpdateRequest request);

    void addAdminRole(RoleAddRequest request);

    void batchEnabled(Integer flag, List<Long> idList);

    RolePermissionResponse getRoleOrgPermission();
}

