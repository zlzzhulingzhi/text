package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.mapper.CourseChapterMapper;
import cn.qbs.wa.teach.course.common.entity.CourseChapter;
import cn.qbs.wa.teach.course.standard.service.CourseChapterService;
import cn.qbs.wa.teach.course.standard.pojo.coursechapter.*;
import cn.qbs.wa.teach.course.standard.service.CourseLessonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 【课程章节】(CourseChapter)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Slf4j
@Service("courseChapterService")
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter> implements CourseChapterService {

    @Resource
    private CourseLessonService courseLessonService;

    @Override
    public CourseChapter add(ChapterAddRequest params) {
        CourseChapter courseChapter = new CourseChapter();
        BeanUtils.copyProperties(params, courseChapter);
        this.save(courseChapter);
        return courseChapter;
    }

    @Override
    public IPage<CourseChapterPageResponse> page(CourseChapterPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseChapterDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseChapterUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseChapter courseChapter = new CourseChapter();
        BeanUtils.copyProperties(params, courseChapter);
        return this.updateById(courseChapter);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        List<CourseLesson> lessons = courseLessonService.lambdaQuery().in(CourseLesson::getChapterId, idList).list();
        // 章节下存在讲次则不允许删除
        if (!lessons.isEmpty()) {
            throw new ServiceException("删除的章节中存在讲次内容，无法删除");
        }
        return this.removeByIds(idList);
    }

    @Override
    public boolean deleteByCourseId(Long courseId) {
        return this.remove(Wrappers.<CourseChapter>lambdaQuery().eq(CourseChapter::getCourseId, courseId));
    }

    @Override
    public boolean incrLessonNumById(Long chapterId, int count) {
        return baseMapper.incrLessonNumById(chapterId, count);
    }

    @Override
    public boolean decrLessonNumById(Long chapterId, int count) {
        return baseMapper.decrLessonNumById(chapterId, count);
    }

    @Override
    public List<CourseChapter> listByCourseId(Long courseId) {
        return this.list(Wrappers.<CourseChapter>lambdaQuery().eq(CourseChapter::getCourseId, courseId));
    }

    @Override
    public CourseChapter createDefault(Long courseId, int lessonNum) {
        CourseChapter chapter = new CourseChapter();
        chapter.setCourseId(courseId);
        chapter.setLessonNum(lessonNum);
        chapter.setChapterName("章");
        this.save(chapter);
        return chapter;
    }
}

