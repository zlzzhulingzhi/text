package cn.qbs.wa.teach.course.standard.pojo.courseattachment;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲义】(CourseAttachment)创建【课程讲义】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
@Data
public class CourseAttachmentAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "讲义ID不能为空")
    @ApiModelProperty(value = "【讲义ID】")
    private Long resourceFileId;

    @ApiModelProperty(value = "【讲义名】")
    private String resourceFileName;

    @ApiModelProperty(value = "【讲义类型】")
    private String resourceFileType;

}

