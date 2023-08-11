package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.mapper.CourseInnerMapper;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoDetailResponse;
import cn.qbs.wa.teach.course.standard.service.CourseInnerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("courseInnerService")
public class CourseInnerServiceImpl implements CourseInnerService {

    @Resource
    private CourseInnerMapper courseInnerMapper;

    @Override
    public Course getById(Long courseId) {
        return courseInnerMapper.getById(courseId);
    }

    @Override
    public CourseInfoDetailResponse courseInfo(Long courseId) {
        return courseInnerMapper.courseInfo(courseId);
    }

    @Override
    public List<Long> getIdByCourseName(CoursePageRequest params) {
        List<CoursePageResponse> coursePageResponseList=courseInnerMapper.getCourseList(params);
        List<Long> courseIdList = coursePageResponseList.stream().map(CoursePageResponse::getId).collect(Collectors.toList());
        return courseIdList;
    }

    @Override
    public List<CourseLesson> getCourseLesson(IdRequest request) {
        List<CourseLesson> courseLessons=courseInnerMapper.getCourseLesson(request.getId());
        return courseLessons;
    }

    @Override
    public List<Course> listByIds(List<Long> idList) {
        return courseInnerMapper.listByIds(idList);
    }
}
