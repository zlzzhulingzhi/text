package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.UniApplicationClient;
import cn.qbs.wa.union.admin.pojo.uniapplicationclient.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用客户端(UniApplicationClient)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
public interface UniApplicationClientService extends IService<UniApplicationClient> {

    /**
     * 新增统一应用客户端
     * @param params
     * @return
     */
    boolean add(UniApplicationClientAddRequest params);

    /**
     * 分页查询统一应用客户端
     * @param params
     * @return
     */
    IPage<UniApplicationClientPageResponse> page(UniApplicationClientPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    UniApplicationClientDetailResponse detail(Serializable id);

    /**
     * 更新统一应用客户端
     * @param params
     * @return
     */
    boolean update(UniApplicationClientUpdateRequest params);

    /**
     * 删除统一应用客户端
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<UniApplicationClientDetailResponse> clientList();
}

