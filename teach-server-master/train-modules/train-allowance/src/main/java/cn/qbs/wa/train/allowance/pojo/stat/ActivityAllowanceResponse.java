package cn.qbs.wa.train.allowance.pojo.stat;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ActivityAllowanceResponse {

    private Long orgId;

    private String orgName;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动主题")
    private String activityTheme;

    @ApiModelProperty(value = " 补贴费用(元)")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    @EncodeContent
    private String contactNumber;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "申请时间")
    private LocalDate applyDate;

    @ApiModelProperty(value = "流程状态 1-审批中 2-通过")
    private Integer flowStatus;

}
