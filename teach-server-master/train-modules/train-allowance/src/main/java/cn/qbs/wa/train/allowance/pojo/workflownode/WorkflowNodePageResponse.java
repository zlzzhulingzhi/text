package cn.qbs.wa.train.allowance.pojo.workflownode;

import cn.qbs.wa.train.allowance.entity.WorkflowNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程节点(WorkflowNode)分页查询流程节点响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
@Data
public class WorkflowNodePageResponse extends WorkflowNode {

    @ApiModelProperty(value = "负责人")
    private String principalInfo;
}

