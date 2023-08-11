package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemUserOauth;
import cn.qbs.wa.union.admin.pojo.systemuseroauth.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 用户第三方应用登录绑定表(SystemUserOauth)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
public interface SystemUserOauthService extends IService<SystemUserOauth> {

    /**
     * 新增用户第三方应用登录绑定表
     * @param params
     * @return
     */
    boolean add(SystemUserOauthAddRequest params);

    /**
     * 分页查询用户第三方应用登录绑定表
     * @param params
     * @return
     */
    IPage<SystemUserOauthPageResponse> page(SystemUserOauthPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemUserOauthDetailResponse detail(Serializable id);

    /**
     * 更新用户第三方应用登录绑定表
     * @param params
     * @return
     */
    boolean update(SystemUserOauthUpdateRequest params);

    /**
     * 删除用户第三方应用登录绑定表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

