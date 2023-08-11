package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemSubUser;
import cn.qbs.wa.union.admin.pojo.systemsubuser.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 子用户表(SystemSubUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
public interface SystemSubUserService extends IService<SystemSubUser> {

    /**
     * 新增子用户表
     * @param params
     * @return
     */
    boolean add(SystemSubUserAddRequest params);

    /**
     * 分页查询子用户表
     * @param params
     * @return
     */
    IPage<SystemSubUserPageResponse> page(SystemSubUserPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemSubUserDetailResponse detail(Serializable id);

    /**
     * 更新子用户表
     * @param params
     * @return
     */
    boolean update(SystemSubUserUpdateRequest params);

    /**
     * 删除子用户表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

