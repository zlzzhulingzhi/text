package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseComponentAttachment;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.MyCourseStudyingDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.ResourceFileDurationRecordDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲次内容附件】(CourseComponentAttachment)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
public interface CourseComponentAttachmentService extends IService<CourseComponentAttachment> {

    /**
     * 新增【课程讲次内容附件】
     * @param params
     * @return
     */
    boolean add(CourseComponentAttachmentAddRequest params);

    /**
     * 分页查询【课程讲次内容附件】
     * @param params
     * @return
     */
    IPage<CourseComponentAttachmentPageResponse> page(CourseComponentAttachmentPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseComponentAttachmentDetailResponse detail(Serializable id);

    /**
     * 更新【课程讲次内容附件】
     * @param params
     * @return
     */
    boolean update(CourseComponentAttachmentUpdateRequest params);

    /**
     * 删除【课程讲次内容附件】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 删除【课程讲次内容附件】
     * @param courseId 课程ID
     * @return
     */
    boolean deleteByCourseId(Long courseId);

    void deleteByLessonId(Long lessonId);

    void deleteByComponentId(List<Long> componentIds);

    List<CourseComponentAttachment> getByComponentId(Long componentId);

   void resourceFileDurationRecording(ResourceFileDurationRecordDTO params);
}

