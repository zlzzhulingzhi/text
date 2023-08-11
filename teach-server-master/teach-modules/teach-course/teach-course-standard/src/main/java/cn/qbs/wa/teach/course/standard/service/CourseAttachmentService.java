package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseAttachment;
import cn.qbs.wa.teach.course.standard.pojo.courseattachment.*;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.ComponentChangeNameRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲义】(CourseAttachment)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
public interface CourseAttachmentService extends IService<CourseAttachment> {

    /**
     * 新增【课程讲义】
     * @param params
     * @return
     */
    boolean add(CourseAttachmentAddRequest params);

    /**
     * 分页查询【课程讲义】
     * @param params
     * @return
     */
    IPage<CourseAttachmentPageResponse> page(CourseAttachmentPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseAttachmentDetailResponse detail(Serializable id);

    /**
     * 更新【课程讲义】
     * @param params
     * @return
     */
    boolean update(CourseAttachmentUpdateRequest params);

    /**
     * 删除【课程讲义】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<CourseAttachmentPageResponse> list(CourseAttachmentListRequest params);

    List<CourseAttachmentPageResponse> listByCourseId(Long courseId);

    boolean addBatch(CourseAttachmentAddBatchRequest params);

    /**
     * 修改课程讲义名称
     * @param params
     * @return
     */
    boolean changeName(ComponentChangeNameRequest params);

}

