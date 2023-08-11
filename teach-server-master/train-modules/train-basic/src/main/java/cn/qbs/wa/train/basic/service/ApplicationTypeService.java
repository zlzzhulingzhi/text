package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.ApplicationType;
import cn.qbs.wa.train.basic.pojo.applicationtype.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * (ApplicationType)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 19:14:27
 */
public interface ApplicationTypeService extends IService<ApplicationType> {

    /**
     * 新增
     * @param params
     * @return
     */
    boolean add(ApplicationTypeAddRequest params);

    /**
     * 分页查询
     * @param params
     * @return
     */
    IPage<ApplicationTypePageResponse> page(ApplicationTypePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    ApplicationTypeDetailResponse detail(Serializable id);

    /**
     * 更新
     * @param params
     * @return
     */
    boolean update(ApplicationTypeUpdateRequest params);

    /**
     * 删除
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

