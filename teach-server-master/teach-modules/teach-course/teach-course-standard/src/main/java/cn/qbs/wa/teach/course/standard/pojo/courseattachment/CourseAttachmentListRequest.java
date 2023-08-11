package cn.qbs.wa.teach.course.standard.pojo.courseattachment;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲义】(CourseAttachment)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
@Data
public class CourseAttachmentListRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲义名】")
    private String resourceFileName;

    @ApiModelProperty(value = "【讲义类型】")
    private String resourceFileType;

}

