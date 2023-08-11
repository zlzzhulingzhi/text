package cn.qbs.wa.train.allowance.pojo.approve;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ApplySettleDetailResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "申请主键")
    private Long applyId;

    @ApiModelProperty(value = "机构名称(快照)")
    private String orgName;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    @EncodeContent
    private String contactNumber;

    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "申请事由")
    private String applyReason;

    @ApiModelProperty(value = "审批意见")
    private String approvalComment;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;

    @ApiModelProperty(value = "申请备注")
    private String applyRemark;

    @ApiModelProperty(value = "申请人")
    private String applyUser;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private LocalDate useDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private LocalDate useDateEnd;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "教室备注")
    private String classroomRemark;

    @ApiModelProperty(value = "宿舍备注")
    private String dormitoryRemark;

    @ApiModelProperty(value = "单据流程状态 1-审批中 2-通过 3-挂起 4-驳回")
    private Integer flowStatus;

    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    @ApiModelProperty(value = "教室申请项")
    private List<ApplyClassroomDTO> classroomList;

    @ApiModelProperty(value = "宿舍申请项")
    private List<ApplyDormitoryDTO> dormitoryList;

}
