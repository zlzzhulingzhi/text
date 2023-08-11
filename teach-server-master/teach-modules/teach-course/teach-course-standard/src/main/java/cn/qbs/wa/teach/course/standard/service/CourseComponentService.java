package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentExtraDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲次内容】(CourseComponent)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseComponentService extends IService<CourseComponent> {

    /**
     * 新增【课程讲次内容】
     * @param params
     * @return
     */
    CourseComponent add(ComponentAddRequest params);

    /**
     * 分页查询【课程讲次内容】
     * @param params
     * @return
     */
    IPage<CourseComponentPageResponse> page(CourseComponentPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseComponentDetailResponse detail(Serializable id);

    /**
     * 更新【课程讲次内容】
     * @param params
     * @return
     */
    boolean update(CourseComponentUpdateRequest params);

    /**
     * 删除【课程讲次内容】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 删除【课程讲次内容】
     * @param courseId 课程ID
     * @return
     */
    boolean deleteByCourseId(Long courseId);

    void deleteByLessonId(Long lessonId);

    void deleteByObjs(List<CourseComponent> courseComponents);

    List<CourseComponent> listByCourseId(Long courseId);

    List<CourseComponent> listByLessonId(Long lessonId);

    List<CourseComponentDTO> listByLesson(Long lessonId);

    List<CourseComponentExtraDTO> listByLessonV2(Long lessonId);

    /**
     * 修改【课程讲次内容名称】
     * @param params
     * @return
     */
    boolean changeName(ComponentChangeNameRequest params);

}

