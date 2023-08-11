package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.LastLearnedDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【学员课程课次信息】(CourseStudentLesson)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-07 14:16:23
 */
public interface CourseStudentLessonService extends IService<CourseStudentLesson> {

    /**
     * 新增【学员课程课次信息】
     * @param params
     * @return
     */
    boolean add(CourseStudentLessonAddRequest params);

    /**
     * 分页查询【学员课程课次信息】
     * @param params
     * @return
     */
    IPage<CourseStudentLessonPageResponse> page(CourseStudentLessonPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseStudentLessonDetailResponse detail(Serializable id);

    /**
     * 更新【学员课程课次信息】
     * @param params
     * @return
     */
    boolean update(CourseStudentLessonUpdateRequest params);

    /**
     * 删除【学员课程课次信息】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 添加学员报名学习课程内容
     * @param courseId 课程ID
     * @param userId 用户ID
     */
    void addByCourseIdAndUser(Long courseId, Long userId);

    /**
     * 删除学员报名学习课程内容
     * @param courseId 课程ID
     * @param userId 用户ID
     */
    void deleteByCourseIdAndUser(Long courseId, Long userId);

    /**
     * 删除学员报名学习课程内容
     * @param componentId 讲次ID
     * @param userId 用户ID
     */
    void deleteByComponentIdAndUser(Long componentId, Long userId);

    CourseStudentLesson getStudentCourseLesson(Long userId, Long componentId);

    List<CourseStudentLesson> getStudentCourseAllLesson(Long userId, Long courseId);

    List<CourseStudentLesson> getUsefulCourseAllLesson(Long userId, Long courseId);

    LastLearnedDTO selectBusinessName(Long userId, Long courseId, Long componentId);
}

