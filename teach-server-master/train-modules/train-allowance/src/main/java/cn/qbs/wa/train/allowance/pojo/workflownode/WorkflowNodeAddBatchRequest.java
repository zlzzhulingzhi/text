package cn.qbs.wa.train.allowance.pojo.workflownode;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程节点(WorkflowNode)创建流程节点参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
@Data
public class WorkflowNodeAddBatchRequest {
    
    @ApiModelProperty(value = "流程ID")
    @NotNull(message = "流程ID不能为空")
    private Long workflowId;

    @ApiModelProperty(value = "流程节点列表")
    private List<WorkflowNodeUpdateRequest> workflowNodeUpdateRequestList;


}

