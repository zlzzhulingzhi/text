package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApplyExpertAttachListRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "id")
    Long id;

    @ApiModelProperty("申请类型")
    private String applyType;

}
