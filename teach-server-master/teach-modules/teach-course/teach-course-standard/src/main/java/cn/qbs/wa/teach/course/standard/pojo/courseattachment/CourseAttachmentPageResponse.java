package cn.qbs.wa.teach.course.standard.pojo.courseattachment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseAttachment;

/**
 * 【课程讲义】(CourseAttachment)分页查询【课程讲义】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
@Data
public class CourseAttachmentPageResponse extends CourseAttachment {

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "音视频的时长")
    private Integer length;

}

