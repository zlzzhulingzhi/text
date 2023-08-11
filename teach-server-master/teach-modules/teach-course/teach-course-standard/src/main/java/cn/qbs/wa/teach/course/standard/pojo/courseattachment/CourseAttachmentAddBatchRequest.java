package cn.qbs.wa.teach.course.standard.pojo.courseattachment;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 【课程讲义】(CourseAttachment)创建【课程讲义】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
@Data
public class CourseAttachmentAddBatchRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @Valid
    @NotEmpty(message = "讲义ID不能为空")
    List<CourseAttachmentAddRequest> attachList;

}

