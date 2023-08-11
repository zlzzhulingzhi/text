package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.ClazzLesson;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 班级课程(ClazzLesson)表服务接口
 *
 * @author makejava
 * @since 2023-03-13 20:12:56
 */
public interface ClazzLessonService extends IService<ClazzLesson> {

    /**
     * 新增班级课程
     *
     * @param params
     * @return
     */
    boolean add(ClazzLessonAddRequest params);

    /**
     * 分页查询班级课程
     *
     * @param params
     * @return
     */
    IPage<ClazzLessonPageResponse> page(ClazzLessonPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClazzLessonDetailResponse detail(Serializable id);

    /**
     * 更新班级课程
     *
     * @param params
     * @return
     */
    boolean update(ClazzLessonUpdateRequest params);

    boolean sort(ClazzLessonUpdateRequest params);

    /**
     * 删除班级课程
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean addBatch(List<ClazzLessonAddRequest> params);

}

