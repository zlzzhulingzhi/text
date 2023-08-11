package cn.qbs.wa.train.allowance.pojo.workflowexecute;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程审批请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowEndRequest extends WorkflowExecuteRequest {

    /**
     * true-通过 false-未通过
     */
    private Boolean pass;

}
