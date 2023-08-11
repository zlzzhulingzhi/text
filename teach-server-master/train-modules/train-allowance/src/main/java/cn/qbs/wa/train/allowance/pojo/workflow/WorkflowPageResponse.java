package cn.qbs.wa.train.allowance.pojo.workflow;

import cn.qbs.wa.train.allowance.entity.Workflow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程定义(Workflow)分页查询流程定义响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
@Data
public class WorkflowPageResponse extends Workflow {

    @ApiModelProperty(value = "")
    private String[] children;
}

