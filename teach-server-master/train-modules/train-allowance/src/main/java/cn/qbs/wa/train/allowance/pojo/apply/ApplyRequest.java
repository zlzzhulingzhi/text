package cn.qbs.wa.train.allowance.pojo.apply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 申请表(Apply)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:40:02
 */
@Data
public class ApplyRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty("申请类型")
    private String applyType;

    @ApiModelProperty("流程代号")
    private String flowCode;

    @ApiModelProperty("节点代号")
    private String nodeCode;

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

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    /* FIXME 以下字段将废弃 审批业务流程相关对象 请在 approve 报下创建 */
    @ApiModelProperty(value = "流程是否结束  0  未结束  1结束")
    private Integer ended;

    @ApiModelProperty(value = "角色id集合")
    private List<Long> roleIdList;

    @ApiModelProperty(value = "流程编号集合")
    private List<String> processNos;

    @ApiModelProperty("过滤未通过单据")
    private Integer flag;

}

