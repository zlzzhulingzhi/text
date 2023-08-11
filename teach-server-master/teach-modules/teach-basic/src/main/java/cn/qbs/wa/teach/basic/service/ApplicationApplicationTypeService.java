package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.entity.ApplicationApplicationType;
import cn.qbs.wa.teach.basic.pojo.applicationapplicationtype.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * (ApplicationApplicationType)表服务接口
 *
 * @author makejava
 * @since 2021-11-10 10:22:56
 */
public interface ApplicationApplicationTypeService extends IService<ApplicationApplicationType> {

    /**
     * 新增
     * @param params
     * @return
     */
    boolean add(ApplicationApplicationTypeAddRequest params);

    /**
     * 分页查询
     * @param params
     * @return
     */
    IPage<ApplicationApplicationTypePageResponse> page(ApplicationApplicationTypePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    ApplicationApplicationTypeDetailResponse detail(Serializable id);

    /**
     * 更新
     * @param params
     * @return
     */
    boolean update(ApplicationApplicationTypeUpdateRequest params);

    /**
     * 删除
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

