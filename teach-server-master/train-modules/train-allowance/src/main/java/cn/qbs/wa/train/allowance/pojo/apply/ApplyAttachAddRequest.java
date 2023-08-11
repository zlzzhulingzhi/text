package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApplyAttachAddRequest {

    @NotNull(message = "申请ID不为空")
    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @NotEmpty(message = "附件列表不为空")
    @ApiModelProperty("附件列表")
    private List<ApplyAttachRequest> attachList;

}
