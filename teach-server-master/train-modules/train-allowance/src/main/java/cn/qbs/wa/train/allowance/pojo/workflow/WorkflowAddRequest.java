package cn.qbs.wa.train.allowance.pojo.workflow;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 流程定义(Workflow)创建流程定义参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:58:46
 */
@Data
public class WorkflowAddRequest {
    
    @ApiModelProperty(value = "流程代号")
    @NotBlank(message = "流程代号不能为空")
    private String flowCode;
    
    @ApiModelProperty(value = "流程名称")
    @NotBlank(message = "流程名称不能为空")
    private String flowName;
    
    @ApiModelProperty(value = "流程描述")
    private String flowDesc;
    
    @ApiModelProperty(value = "排序序号")
    private Integer sort;
    
    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

}

