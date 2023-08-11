package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.pojo.courselesson.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲次】(CourseLesson)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseLessonService extends IService<CourseLesson> {

    /**
     * 新增【课程讲次】
     * @param params
     * @return
     */
    CourseLesson add(LessonAddRequest params);

    /**
     * 分页查询【课程讲次】
     * @param params
     * @return
     */
    IPage<CourseLessonPageResponse> page(CourseLessonPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseLessonDetailResponse detail(Serializable id);

    /**
     * 更新【课程讲次】
     * @param params
     * @return
     */
    boolean update(CourseLessonUpdateRequest params);

    /**
     * 删除【课程讲次】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 删除【课程讲次】
     * @param courseId 课程ID
     * @return
     */
    boolean deleteByCourseId(Long courseId);

    /**
     * 根据课程ID获取课次列表
     * @param courseId 课程ID
     * @return 课程列表
     */
    List<CourseLesson> listByCourseId(Long courseId);

    List<CourseLesson> listByChapterId(Long chapterId);

    CourseLesson createDefault(Long courseId, Long chapterId);
}

