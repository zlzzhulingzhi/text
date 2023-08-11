package cn.qbs.wa.train.allowance.pojo.workflowexecute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowResult {

    /**
     * 下一个流程节点
     */
    private Long nextNodeId;

    /**
     * 流程是否结束(1-结束，0-未结束)
     */
    private Integer ended;

}
