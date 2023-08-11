package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QualificationProjectAddRequest extends QualificationProjectRequest {

    @ApiModelProperty(value = "资质申请表ID")
    private Long qualificationId;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

}
