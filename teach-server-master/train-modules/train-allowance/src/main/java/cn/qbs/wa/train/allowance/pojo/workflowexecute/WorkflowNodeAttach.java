package cn.qbs.wa.train.allowance.pojo.workflowexecute;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkflowNodeAttach {

    @ApiModelProperty(value = "附件名称")
    private String attachName;

    @ApiModelProperty(value = "附件地址")
    private String attachUrl;
}
