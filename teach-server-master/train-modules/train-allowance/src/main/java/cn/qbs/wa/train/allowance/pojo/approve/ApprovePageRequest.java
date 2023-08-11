package cn.qbs.wa.train.allowance.pojo.approve;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 申请表(Apply)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:40:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty("申请类型")
    private String applyType;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private LocalDate applyDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private LocalDate applyDateEnd;

    @ApiModelProperty(value = "申请结果 0-不通过 1-通过")
    private Integer applyResult;

    @ApiModelProperty(value = "单据流程状态 1-审批中 2-通过 3-挂起 4-驳回")
    private Integer flowStatus;

    @ApiModelProperty(value = "角色集合")
    private Set<String> roles;

    @ApiModelProperty(value = "流程编号集合")
    private List<String> processNos;

    @ApiModelProperty("过滤未通过单据")
    private Integer flag;

    @ApiModelProperty(value = "申请结果 11-待审核 21-已审核")
    private Integer status;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("活动名称")
    private String activityName;
}

