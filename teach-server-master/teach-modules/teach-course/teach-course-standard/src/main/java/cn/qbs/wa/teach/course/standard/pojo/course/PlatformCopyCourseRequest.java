package cn.qbs.wa.teach.course.standard.pojo.course;

import cn.qbs.wa.teach.common.core.domain.PlatformIdRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlatformCopyCourseRequest extends PlatformIdRequest {

    @NotNull(message = "机构ID不能为空")
    private Long toOrgId;

    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(value = "【课程名称】")
    private String courseName;
}
