package cn.qbs.wa.train.allowance.pojo.applyactivity;


import cn.qbs.wa.train.allowance.entity.ApplyActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)更新资助资金申请-学术会议和竞赛活动参数
 *
 * @author makejava
 * @since 2022-11-03 19:27:15
 */
@Data
public class ApplyActivityUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @ApiModelProperty(value = "机构性质(字典值)")
    private String orgCategory;

    @ApiModelProperty(value = "机构简介")
    private String orgIntro;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "法人代表")
    private String legalPerson;

    @ApiModelProperty(value = "法人电话")
    private String legalNumber;

    @ApiModelProperty(value = "法人邮箱")
    private String legalEmail;

    @ApiModelProperty(value = "申请类型(settle-入驻申请 qualification-资格申请  allowance-资助申请)")
    private String applyType;

    @ApiModelProperty(value = "申请事由")
    private String applyReason;

    @ApiModelProperty(value = "申请状态 0-未提交 1-已提交")
    private Integer applyStatus;

    @ApiModelProperty(value = "申请时间(申请状态为`已提交`时有值)")
    private LocalDate applyDate;

    @ApiModelProperty(value = "申请人ID(申请状态为`已提交`时有值)")
    private Long applyUser;

    @ApiModelProperty(value = "申请备注")
    private String applyRemark;

    @ApiModelProperty(value = "申请结果 0-不通过 1-通过")
    private Integer applyResult;

    @ApiModelProperty(value = "审批意见")
    private String approvalComment;

    @ApiModelProperty(value = "流程实例编号")
    private String processNo;

    @ApiModelProperty(value = "流程代号")
    private String flowCode;

    @ApiModelProperty(value = "流程状态 1-审批中 2-通过 3-挂起 4-驳回 ")
    private Integer flowStatus;

    @ApiModelProperty(value = "学术活动数据")
    private ApplyActivity applyActivity;

}

