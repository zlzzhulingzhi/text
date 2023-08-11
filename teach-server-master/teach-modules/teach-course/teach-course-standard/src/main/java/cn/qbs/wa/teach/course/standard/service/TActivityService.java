package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.entity.TActivity;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 活动表(TActivity)表服务接口
 *
 * @author makejava
 * @since 2022-12-13 15:55:03
 */
public interface TActivityService extends IService<TActivity> {

    /**
     * 新增活动表
     *
     * @param params
     * @return
     */
    boolean add(TActivityAddRequest params);

    /**
     * 分页查询活动表
     *
     * @param params
     * @return
     */
    IPage<TActivityPageResponse> page(TActivityPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    TActivityDetailResponse detail(Serializable id);

    /**
     * 更新活动表
     *
     * @param params
     * @return
     */
    boolean update(TActivityUpdateRequest params);

    /**
     * 删除活动表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean updateShelfStatus(TActivityUpdateShelfStatusRequest params);

}

