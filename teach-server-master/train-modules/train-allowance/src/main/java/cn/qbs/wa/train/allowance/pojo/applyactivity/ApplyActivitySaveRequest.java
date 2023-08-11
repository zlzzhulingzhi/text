package cn.qbs.wa.train.allowance.pojo.applyactivity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplyActivitySaveRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "申请类型(settle-入驻申请 qualification-资格申请  allowance-资助申请)")
    private String applyType;

    @ApiModelProperty(value = "申请事由")
    private String applyReason;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;

    @ApiModelProperty(value = "申请备注")
    private String applyRemark;

    @ApiModelProperty(value = "流程实例编号")
    private String processNo;

    @ApiModelProperty(value = "流程代号")
    private String flowCode;

    @ApiModelProperty(value = "当前所处流程节点")
    private Long curNodeId;

    @ApiModelProperty(value = "学术会议或活动数据")
    private ApplyActivityAddRequest applyActivityAddRequest;
}
