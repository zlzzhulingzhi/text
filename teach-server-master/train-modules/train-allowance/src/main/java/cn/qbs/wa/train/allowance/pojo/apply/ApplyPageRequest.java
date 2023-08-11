package cn.qbs.wa.train.allowance.pojo.apply;


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
public class ApplyPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private LocalDate applyDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private LocalDate applyDateEnd;

    @ApiModelProperty(value = "申请结果 0-不通过 1-通过")
    private Integer applyResult;

    @ApiModelProperty(value = "申请结果 11-待审核 21-已审核")
    private Integer status;

    @ApiModelProperty(value = "流程编号集合")
    private List<String> processNos;

    @ApiModelProperty("节点代号")
    private String nodeCode;

    @ApiModelProperty("申请类型")
    private String applyType;

    @ApiModelProperty("过滤未通过单据")
    private Integer flag;

    @ApiModelProperty("流程代号")
    private String flowCode;

    @ApiModelProperty(value = "单据流程状态 1-审批中 2-通过 3-挂起 4-驳回")
    private Integer flowStatus;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "角色集合")
    private Set<String> roles;

    @ApiModelProperty(value = "申请状态 0-未提交 1-已提交")
    private Integer applyStatus;
}

