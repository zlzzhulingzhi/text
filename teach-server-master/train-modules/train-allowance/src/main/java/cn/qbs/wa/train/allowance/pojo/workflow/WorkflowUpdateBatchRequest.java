package cn.qbs.wa.train.allowance.pojo.workflow;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 流程定义(Workflow)更新流程定义参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
@Data
public class WorkflowUpdateBatchRequest {

    @ApiModelProperty(value = "主键")
    private List<Long> ids;
    
    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

}

