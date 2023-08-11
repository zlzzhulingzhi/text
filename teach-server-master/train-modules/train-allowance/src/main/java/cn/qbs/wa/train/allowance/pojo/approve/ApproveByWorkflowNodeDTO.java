package cn.qbs.wa.train.allowance.pojo.approve;

import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowInstanceNodeResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApproveByWorkflowNodeDTO {

    @ApiModelProperty(value = "单据流程节点数据")
    private List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses;
}
