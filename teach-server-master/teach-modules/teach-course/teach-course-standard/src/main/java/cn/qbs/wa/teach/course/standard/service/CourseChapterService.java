package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseChapter;
import cn.qbs.wa.teach.course.standard.pojo.coursechapter.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程章节】(CourseChapter)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseChapterService extends IService<CourseChapter> {

    /**
     * 新增【课程章节】
     * @param params
     * @return
     */
    CourseChapter add(ChapterAddRequest params);

    /**
     * 分页查询【课程章节】
     * @param params
     * @return
     */
    IPage<CourseChapterPageResponse> page(CourseChapterPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseChapterDetailResponse detail(Serializable id);

    /**
     * 更新【课程章节】
     * @param params
     * @return
     */
    boolean update(CourseChapterUpdateRequest params);

    /**
     * 删除【课程章节】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 删除【课程章节】
     * @param courseId 课程ID
     * @return
     */
    boolean deleteByCourseId(Long courseId);

    boolean incrLessonNumById(Long chapterId, int i);

    boolean decrLessonNumById(Long chapterId, int i);

    List<CourseChapter> listByCourseId(Long courseId);

    CourseChapter createDefault(Long courseId, int lessonNum);
}

