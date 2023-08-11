package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseComment;
import cn.qbs.wa.teach.course.standard.pojo.coursecomment.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程评价】(CourseComment)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseCommentService extends IService<CourseComment> {

    /**
     * 新增【课程评价】
     * @param params
     * @return
     */
    boolean add(CourseCommentAddRequest params);

    /**
     * 分页查询【课程评价】
     * @param params
     * @return
     */
    IPage<CourseCommentPageResponse> page(CourseCommentPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseCommentDetailResponse detail(Serializable id);

    /**
     * 更新【课程评价】
     * @param params
     * @return
     */
    boolean update(CourseCommentUpdateRequest params);

    /**
     * 删除【课程评价】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 逻辑删除【课程评价】
     * @param idList 课程评价ID列表
     * @return 结果
     */
    boolean deleteLogicByIds(List<Long> idList);
}

