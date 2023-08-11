package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.UniApplication;
import cn.qbs.wa.union.admin.pojo.uniapplication.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用展示(UniApplication)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
public interface UniApplicationService extends IService<UniApplication> {

    /**
     * 新增统一应用展示
     * @param params
     * @return
     */
    boolean add(UniApplicationAddRequest params);

    /**
     * 分页查询统一应用展示
     * @param params
     * @return
     */
    IPage<UniApplicationPageResponse> page(UniApplicationPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    UniApplicationDetailResponse detail(Serializable id);

    /**
     * 更新统一应用展示
     * @param params
     * @return
     */
    boolean update(UniApplicationUpdateRequest params);

    /**
     * 删除统一应用展示
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

