package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.courseattachment.CourseAttachmentListRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseAttachment;
import cn.qbs.wa.teach.course.standard.pojo.courseattachment.CourseAttachmentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseattachment.CourseAttachmentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseattachment.CourseAttachmentPageResponse;

/**
 * 【课程讲义】(CourseAttachment)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
public interface CourseAttachmentMapper extends BaseMapper<CourseAttachment> {

    IPage<CourseAttachmentPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseAttachmentPageRequest params);

    List<CourseAttachmentPageResponse> list(@Param("params") CourseAttachmentListRequest params);

    CourseAttachmentDetailResponse selectDetailById(@Param("id") Serializable id);

}

