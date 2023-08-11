package cn.qbs.wa.train.allowance.pojo.workflowexecute;

import lombok.Data;

import java.util.List;

/**
 * 流程审批请求
 */
@Data
public class WorkflowExecuteRequest {

    /**
     * 流程实例编号
     */
    private String processNo;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 审批附件
     */
    private List<WorkflowNodeAttach> attachList;

}
