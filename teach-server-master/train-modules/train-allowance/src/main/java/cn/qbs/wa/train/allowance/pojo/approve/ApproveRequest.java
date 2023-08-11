package cn.qbs.wa.train.allowance.pojo.approve;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowNodeAttach;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApproveRequest extends IdRequest {

  @ApiModelProperty("申请表主键")
  private Long applyId;

  @ApiModelProperty("当前流程节点ID")
  private String curNodeId;

  @ApiModelProperty("评审结果 0-不通过 1-通过")
  private Integer result;

  @ApiModelProperty("评审意见")
  private String comment;

  @ApiModelProperty("审批附件")
  private List<WorkflowNodeAttach> attachList;

  @ApiModelProperty(value = "申请状态 0-未提交 1-已提交")
  private Integer applyStatus;
}
