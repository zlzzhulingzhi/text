package cn.qbs.wa.train.allowance.pojo.workflownode;

import cn.qbs.wa.train.allowance.enums.FlowNodeStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkflowInstanceNodeResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "流程ID")
    private Long workflowId;

    @ApiModelProperty(value = "流程实例编号")
    private String processNo;

    @ApiModelProperty(value = "节点代号(实例化)")
    private String nodeCode;

    @ApiModelProperty(value = "流程节点(实例化)")
    private String nodeName;

    @ApiModelProperty(value = "主办类型(role-角色 user-用户)")
    private String managerType;

    @ApiModelProperty(value = "主办类型关联对象(角色使用code, 用户使用userId)")
    private String managerRef;

    @ApiModelProperty(value = "当前节点审批人信息")
    private String managerInfo;

    @ApiModelProperty(value = "上一个流程节点ID")
    private Long prevId;

    @ApiModelProperty(value = "下一个流程节点ID")
    private Long nextId;

    @ApiModelProperty(value = "状态(10-未开始 11-进行中 21-通过 22-未通过)")
    private Integer status;

    @ApiModelProperty(value = "审批意见")
    private String comment;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "审批人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "审批时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "审批时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime approveApplyTime;

    public LocalDateTime getApproveApplyTime() {
        if (status.equals(FlowNodeStatusEnum.IN_PROGRESS.getCode())) {
            return null;
        } else {
            return this.updateTime;
        }
    }
}
