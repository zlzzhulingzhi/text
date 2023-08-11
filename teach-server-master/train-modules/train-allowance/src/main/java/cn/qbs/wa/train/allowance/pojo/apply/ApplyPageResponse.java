package cn.qbs.wa.train.allowance.pojo.apply;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 申请表(Apply)分页查询申请表响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:40:03
 */
@Data
public class ApplyPageResponse {

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
    @EncodeContent
    private String contactNumber;

    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "法人代表")
    private String legalPerson;

    @ApiModelProperty(value = "法人电话")
    @EncodeContent
    private String legalNumber;

    @ApiModelProperty(value = "法人邮箱")
    private String legalEmail;

    @ApiModelProperty(value = "申请类型(settle-入驻申请 qualification-资格申请  allowance-资助申请)")
    private String applyType;

    @ApiModelProperty(value = "申请事由")
    private String applyReason;

    @ApiModelProperty(value = "审批意见")
    private String approvalComment;

    @ApiModelProperty(value = "申请状态 0-未提交 1-已提交")
    private Integer applyStatus;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间(申请状态为`已提交`时有值)")
    private LocalDate applyDate;

    @ApiModelProperty(value = "申请人ID(申请状态为`已提交`时有值)")
    private Long applyUser;

    @ApiModelProperty(value = "申请备注")
    private String applyRemark;

    @ApiModelProperty(value = "申请结果 0-不通过 1-通过")
    private Integer applyResult;

    @ApiModelProperty(value = "流程实例编号")
    private String processNo;

    @ApiModelProperty(value = "流程代号")
    private String flowCode;

    @ApiModelProperty(value = "流程状态 1-审批中 2-通过 3-挂起 4-驳回 ")
    private Integer flowStatus;

    @ApiModelProperty(value = "申请人")
    private String applyUserName;

    @ApiModelProperty(value = "审核状态 11-未结束 21-已结束")
    private Integer status;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动主题")
    private String activityTheme;

    @ApiModelProperty(value = " 活动预算金额(元)")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "项目名称(课程名称)")
    private String projectName;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "合计补贴费用")
    private BigDecimal totalAllowanceFee;

    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    @ApiModelProperty(value = "当前实例节点id")
    private String curNodeId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

}

