package cn.qbs.wa.teach.course.standard.pojo.dto.lite;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignUpDTO {

    @NotNull(message = "课程ID不为空")
    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @NotNull(message = "机构ID不为空")
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @NotNull(message = "表单信息不能为空")
    @ApiModelProperty(value = "学员表单信息")
    private MemberFormDTO register;
}
