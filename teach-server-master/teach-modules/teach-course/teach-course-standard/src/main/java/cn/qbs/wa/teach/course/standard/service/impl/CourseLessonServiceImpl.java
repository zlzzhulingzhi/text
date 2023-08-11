package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.mapper.CourseLessonMapper;
import cn.qbs.wa.teach.course.standard.pojo.courselesson.*;
import cn.qbs.wa.teach.course.standard.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【课程讲次】(CourseLesson)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Slf4j
@Service("courseLessonService")
public class CourseLessonServiceImpl extends ServiceImpl<CourseLessonMapper, CourseLesson> implements CourseLessonService {

    @Resource
    private CourseChapterService courseChapterService;

    @Resource
    private CourseComponentService courseComponentService;

    @Resource
    private CourseComponentAttachmentService courseComponentAttachmentService;

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CourseLesson add(LessonAddRequest params) {
        CourseLesson courseLesson = new CourseLesson();
        BeanUtils.copyProperties(params, courseLesson);
        this.save(courseLesson);
        // 课程章所包含的讲次数 +1
        courseChapterService.incrLessonNumById(params.getChapterId(), 1);
        return courseLesson;
    }

    @Override
    public IPage<CourseLessonPageResponse> page(CourseLessonPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseLessonDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseLessonUpdateRequest params) {
        CourseLesson courseLesson = new CourseLesson();
        BeanUtils.copyProperties(params, courseLesson);
        return this.updateById(courseLesson);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Long> idList) {
        List<CourseLesson> lessons = this.listByIds(idList);
        if (lessons.isEmpty()) {
            return false;
        }
        this.removeByIds(idList);

        Map<Long, Integer> chapterMap = lessons.stream().collect(Collectors.groupingBy(CourseLesson::getChapterId, Collectors.reducing(0, e -> -1, Integer::sum)));
        // 课程章所包含的讲次数 -1
        chapterMap.forEach((chapterId, count) -> courseChapterService.decrLessonNumById(chapterId, count));

        List<CourseComponent> components = courseComponentService.lambdaQuery().in(CourseComponent::getLessonId, idList).list();
        if (!components.isEmpty()) {
            throw new ServiceException("删除的节中存在讲次内容，无法删除");
        }
        // 减去课程所包含的课时数
        //Long courseId = lessons.get(0).getCourseId();
        //courseStatisticOverviewService.incrLessonNum(courseId, components.size());
        //
        //// 删除附件
        //courseComponentService.deleteByObjs(components);
        //courseComponentAttachmentService.deleteByComponentId(components.stream().map(CourseComponent::getId).collect(Collectors.toList()));

        return true;
    }

    @Override
    public boolean deleteByCourseId(Long courseId) {
        return this.remove(Wrappers.<CourseLesson>lambdaQuery().eq(CourseLesson::getCourseId, courseId));
    }

    @Override
    public List<CourseLesson> listByCourseId(Long courseId) {
        return this.lambdaQuery().eq(CourseLesson::getCourseId, courseId).list();
    }

    @Override
    public List<CourseLesson> listByChapterId(Long chapterId) {
        return this.lambdaQuery().eq(CourseLesson::getChapterId, chapterId).list();
    }

    @Override
    public CourseLesson createDefault(Long courseId, Long chapterId) {
        CourseLesson lesson = new CourseLesson();
        lesson.setChapterId(chapterId);
        lesson.setCourseId(courseId);
        lesson.setLessonName("节");
        this.save(lesson);
        return lesson;
    }
}

