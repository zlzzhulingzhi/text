package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QualificationProjectRemoveRequest {

    @NotNull(message = "申请ID不能为空")
    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @NotNull(message = "计划项目ID不能为空")
    @ApiModelProperty(value = "计划项目ID")
    private Long projectId;

}
