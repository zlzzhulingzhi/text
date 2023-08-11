package cn.qbs.wa.train.allowance.pojo.workflownode;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程节点(WorkflowNode)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-13 13:59:24
 */
@Data
public class WorkflowNodePageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "流程ID")
    private Long workflowId;
    
    @ApiModelProperty(value = "节点代号")
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

}

