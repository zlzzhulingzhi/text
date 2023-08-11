package cn.qbs.wa.train.allowance.pojo.stat;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrgAllowancePageRequest extends BasePageRequest {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private LocalDate dateStart;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private LocalDate dateEnd;

    @ApiModelProperty("申请类型 allowance-课程  activity-活动")
    private String applyType;

    @ApiModelProperty(value = "审批状态 1-审批中 2-通过")
    private Integer flowStatus;

    @ApiModelProperty(value = "审批状态 1-审批中 2-通过")
    private List<Integer> flowStatusList;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
