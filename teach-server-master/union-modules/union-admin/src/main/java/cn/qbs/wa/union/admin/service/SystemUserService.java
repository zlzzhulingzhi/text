package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemUser;
import cn.qbs.wa.union.admin.pojo.systemuser.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 平台系统子用户表(SystemUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
public interface SystemUserService extends IService<SystemUser> {

    /**
     * 新增平台系统子用户表
     * @param params
     * @return
     */
    boolean add(SystemUserAddRequest params);

    /**
     * 分页查询平台系统子用户表
     * @param params
     * @return
     */
    IPage<SystemUserPageResponse> page(SystemUserPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemUserDetailResponse detail(Serializable id);

    /**
     * 更新平台系统子用户表
     * @param params
     * @return
     */
    boolean update(SystemUserUpdateRequest params);

    /**
     * 删除平台系统子用户表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    SystemUserRoleMenuMineResponse getMinePermission();

    void innerUpdate(InnerSystemUserUpdateRequest params);
}

