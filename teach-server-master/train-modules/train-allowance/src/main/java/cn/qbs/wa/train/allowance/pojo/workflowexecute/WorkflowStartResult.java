package cn.qbs.wa.train.allowance.pojo.workflowexecute;

import lombok.Data;

@Data
public class WorkflowStartResult {

    /**
     * 流程实例编号
     */
    private String processNo;

    /**
     * 下一个流程节点
     */
    private Long curNodeId;

    /**
     * 下一个流程节点
     */
    private String curNodeCode;
}
