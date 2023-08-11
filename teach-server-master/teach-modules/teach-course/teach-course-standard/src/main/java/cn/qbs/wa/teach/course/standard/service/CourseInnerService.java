package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoDetailResponse;

import java.util.List;

/**
 * App端课程业务相关接口
 * @author yjx
 */
public interface CourseInnerService {

    Course getById(Long courseId);

    CourseInfoDetailResponse courseInfo(Long courseId);

    List<Long> getIdByCourseName(CoursePageRequest params);

    List<CourseLesson> getCourseLesson(IdRequest request);

    List<Course> listByIds(List<Long> idList);
}
