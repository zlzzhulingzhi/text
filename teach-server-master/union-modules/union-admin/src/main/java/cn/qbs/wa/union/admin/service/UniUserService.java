package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.UniUser;
import cn.qbs.wa.union.admin.pojo.uniuser.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 统一用户表(UniUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
public interface UniUserService extends IService<UniUser> {

    /**
     * 新增统一用户表
     * @param params
     * @return
     */
    boolean add(UniUserAddRequest params);

    /**
     * 分页查询统一用户表
     * @param params
     * @return
     */
    IPage<UniUserPageResponse> page(UniUserPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    UniUserDetailResponse detail(Serializable id);

    /**
     * 更新统一用户表
     * @param params
     * @return
     */
    boolean update(UniUserUpdateRequest params);

    /**
     * 删除统一用户表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    UniUser addUser(UniUser uniUser);
}

