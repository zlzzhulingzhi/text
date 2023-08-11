package cn.qbs.wa.train.allowance.pojo.workflownode;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程节点(WorkflowNode)创建流程节点参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
@Data
public class WorkflowNodeAddRequest {
    
    @ApiModelProperty(value = "流程ID")
    @NotNull(message = "流程ID不能为空")
    private Long workflowId;
    
    @ApiModelProperty(value = "节点代号")
    @NotBlank(message = "节点代号不能为空")
    private String nodeCode;
    
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    
    @ApiModelProperty(value = "节点描述")
    private String nodeDesc;
    
    @ApiModelProperty(value = "审批人ID")
    private Long userId;
    
    @ApiModelProperty(value = "审批角色ID")
    private Long roleId;
    
    @ApiModelProperty(value = "审批部门ID")
    private Long deptId;
    
    @ApiModelProperty(value = "排序序号")
    private Integer sort;

    @ApiModelProperty(value = "主办类型")
    private String managerType;

    @ApiModelProperty(value = "主办类型引用")
    private String managerRef;

}

