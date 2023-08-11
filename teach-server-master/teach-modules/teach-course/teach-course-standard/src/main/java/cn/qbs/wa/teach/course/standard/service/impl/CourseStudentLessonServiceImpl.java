package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
import cn.qbs.wa.teach.course.standard.mapper.CourseStudentLessonMapper;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.LastLearnedDTO;
import cn.qbs.wa.teach.course.standard.service.CourseComponentService;
import cn.qbs.wa.teach.course.standard.service.CourseLessonService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentLessonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【学员课程课次信息】(CourseStudentLesson)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-07 14:16:23
 */
@Slf4j
@Service("courseStudentLessonService")
public class CourseStudentLessonServiceImpl extends ServiceImpl<CourseStudentLessonMapper, CourseStudentLesson> implements CourseStudentLessonService {

    @Resource
    private CourseLessonService courseLessonService;

    @Resource
    private CourseComponentService courseComponentService;

    @Override
    public boolean add(CourseStudentLessonAddRequest params) {
        CourseStudentLesson courseStudentLesson = new CourseStudentLesson();
        BeanUtils.copyProperties(params, courseStudentLesson);
        return this.save(courseStudentLesson);
    }

    @Override
    public IPage<CourseStudentLessonPageResponse> page(CourseStudentLessonPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseStudentLessonDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseStudentLessonUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseStudentLesson courseStudentLesson = new CourseStudentLesson();
        BeanUtils.copyProperties(params, courseStudentLesson);
        return this.updateById(courseStudentLesson);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void addByCourseIdAndUser(Long courseId, Long userId) {
        List<CourseComponent> components = courseComponentService.listByCourseId(courseId);
        if (!components.isEmpty()) {
            List<CourseStudentLesson> studentLessonList = components.stream().map(component -> {
                CourseStudentLesson courseStudentLesson = new CourseStudentLesson();
                BeanUtils.copyProperties(component, courseStudentLesson, "id", "createBy", "createTime", "updateBy", "updateTime");
                courseStudentLesson.setComponentId(component.getId());
                courseStudentLesson.setUserId(userId);
                return courseStudentLesson;
            }).collect(Collectors.toList());
            // 获取章ID信息
            List<Long> lessonIds = studentLessonList.stream().map(CourseStudentLesson::getLessonId).distinct().collect(Collectors.toList());
            List<CourseLesson> lessons = courseLessonService.listByIds(lessonIds);
            if (!lessons.isEmpty()) {
                // 每节对应章ID
                Map<Long, Long> map = lessons.stream().collect(Collectors.toMap(CourseLesson::getId, CourseLesson::getChapterId));
                studentLessonList.forEach(e -> e.setChapterId(map.get(e.getLessonId())));
            }
            this.saveBatch(studentLessonList.stream().filter(e -> e.getChapterId() != null).collect(Collectors.toList()));
        }
    }

    @Override
    public void deleteByCourseIdAndUser(Long courseId, Long userId) {
        List<CourseStudentLesson> list = this.lambdaQuery().eq(CourseStudentLesson::getCourseId, courseId).eq(CourseStudentLesson::getUserId, userId).list();
        List<Long> idList = list.stream().map(CourseStudentLesson::getId).collect(Collectors.toList());
        if (!idList.isEmpty()) {
            this.deleteByIds(idList);
        }
    }

    @Override
    public void deleteByComponentIdAndUser(Long componentId, Long userId) {
        CourseStudentLesson studentCourseLesson = getStudentCourseLesson(userId, componentId);
        if (studentCourseLesson != null) {
            this.removeById(studentCourseLesson.getId());
        }
    }

    @Override
    public CourseStudentLesson getStudentCourseLesson(Long userId, Long componentId) {
        return this.lambdaQuery().eq(CourseStudentLesson::getUserId, userId).eq(CourseStudentLesson::getComponentId, componentId).one();
    }

    @Override
    public List<CourseStudentLesson> getStudentCourseAllLesson(Long userId, Long courseId) {
        return this.lambdaQuery().eq(CourseStudentLesson::getUserId, userId).eq(CourseStudentLesson::getCourseId, courseId).list();
    }

    @Override
    public List<CourseStudentLesson> getUsefulCourseAllLesson(Long userId, Long courseId) {
        return this.baseMapper.getUsefulCourseAllLesson(userId, courseId);
    }

    @Override
    public LastLearnedDTO selectBusinessName(Long userId, Long courseId, Long componentId) {
        return this.baseMapper.selectBusinessName(userId,courseId,componentId);
    }
}

