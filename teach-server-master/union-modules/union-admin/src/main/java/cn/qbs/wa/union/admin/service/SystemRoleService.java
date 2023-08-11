package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.pojo.systemrole.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统角色】(SystemRole)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
public interface SystemRoleService extends IService<SystemRole> {

    /**
     * 新增【系统角色】
     * @param params
     * @return
     */
    boolean add(SystemRoleAddRequest params);

    /**
     * 分页查询【系统角色】
     * @param params
     * @return
     */
    IPage<SystemRolePageResponse> page(SystemRolePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemRoleDetailResponse detail(Serializable id);

    /**
     * 更新【系统角色】
     * @param params
     * @return
     */
    boolean update(SystemRoleUpdateRequest params);

    /**
     * 删除【系统角色】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

