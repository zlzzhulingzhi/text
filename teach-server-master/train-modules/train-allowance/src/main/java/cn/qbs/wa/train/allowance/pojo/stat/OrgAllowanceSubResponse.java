package cn.qbs.wa.train.allowance.pojo.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrgAllowanceSubResponse extends SummaryAllowanceResponse {

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "项目名称(课程名称、活动名称)")
    private String projectName;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动主题")
    private String activityTheme;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;

    @ApiModelProperty(value = "流程状态 1-审批中 2-通过")
    private Integer flowStatus;

    @ApiModelProperty(value = "课程申请ID")
    private Long id;
}
