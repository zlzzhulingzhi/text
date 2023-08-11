package cn.qbs.wa.train.allowance.pojo.apply;

import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowInstanceNodeResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApplyByWorkflowNodeDTO {

    @ApiModelProperty(value = "单据流程节点数据")
    private List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses;
}
