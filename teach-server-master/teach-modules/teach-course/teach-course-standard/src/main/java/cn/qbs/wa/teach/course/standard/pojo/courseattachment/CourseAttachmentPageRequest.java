package cn.qbs.wa.teach.course.standard.pojo.courseattachment;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲义】(CourseAttachment)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseAttachmentPageRequest extends BasePageRequest {

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

