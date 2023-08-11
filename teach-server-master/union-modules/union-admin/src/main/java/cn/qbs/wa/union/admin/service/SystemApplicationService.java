package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemApplication;
import cn.qbs.wa.union.admin.pojo.systemapplication.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统应用】(SystemApplication)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:02:58
 */
public interface SystemApplicationService extends IService<SystemApplication> {

    /**
     * 新增【系统应用】
     * @param params
     * @return
     */
    boolean add(SystemApplicationAddRequest params);

    /**
     * 分页查询【系统应用】
     * @param params
     * @return
     */
    IPage<SystemApplicationPageResponse> page(SystemApplicationPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SystemApplicationDetailResponse detail(Serializable id);

    /**
     * 更新【系统应用】
     * @param params
     * @return
     */
    boolean update(SystemApplicationUpdateRequest params);

    /**
     * 删除【系统应用】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<SystemApplicationDetailResponse> appList(SystemApplicationListRequest request);
}

