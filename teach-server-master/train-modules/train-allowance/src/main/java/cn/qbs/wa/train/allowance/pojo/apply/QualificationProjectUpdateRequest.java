package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QualificationProjectUpdateRequest extends QualificationProjectRequest {

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "计划项目ID")
    private Long projectId;

}
