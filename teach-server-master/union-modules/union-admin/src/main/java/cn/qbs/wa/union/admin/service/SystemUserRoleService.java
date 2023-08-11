package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.entity.SystemUserRole;
import cn.qbs.wa.union.admin.pojo.systemuserrole.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【平台用户角色关联关系】(SystemUserRole)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
public interface SystemUserRoleService extends IService<SystemUserRole> {

    /**
     * 新增【平台用户角色关联关系】
     * @param params
     * @return
     */
    boolean add(SystemUserRoleAddRequest params);

    /**
     * 分页查询【平台用户角色关联关系】
     * @param params
     * @return
     */
    IPage<SystemUserRolePageResponse> page(SystemUserRolePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemUserRoleDetailResponse detail(Serializable id);

    /**
     * 更新【平台用户角色关联关系】
     * @param params
     * @return
     */
    boolean update(SystemUserRoleUpdateRequest params);

    /**
     * 删除【平台用户角色关联关系】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<SystemRole> listRoleByUserId(Long userId);

    /**
     * 过滤不合法的角色ID
     * @param list
     * @return
     */
    List<SystemRole> filterInvalid(List<SystemRole> list);
}

