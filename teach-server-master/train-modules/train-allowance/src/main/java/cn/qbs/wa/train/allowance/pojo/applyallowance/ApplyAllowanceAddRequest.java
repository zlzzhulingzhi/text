package cn.qbs.wa.train.allowance.pojo.applyallowance;


import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeeAddRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentAddRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)创建资助资金申请-网络安全培训课程参数
 *
 * @author makejava
 * @since 2022-11-03 19:28:57
 */
@Data
public class ApplyAllowanceAddRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "项目编号")
    private String projectNo;

    @ApiModelProperty(value = "项目名称(课程名称)")
    private String projectName;

    @ApiModelProperty(value = "合计补贴费用")
    private BigDecimal totalAllowanceFee;

    @ApiModelProperty(value = "实际参训人数")
    private Integer studentNum;

    @ApiModelProperty(value = "培训开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "培训结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    @ApiModelProperty(value = "考试人数")
    private Integer examNum;

    @ApiModelProperty(value = "考试日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate examDate;

    @ApiModelProperty(value = "班级ID")
    private Long classId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "网络安全培训费用集合")
    private List<ApplyAllowanceFeeAddRequest> applyAllowanceFeeAddRequestList;

    @ApiModelProperty(value = "网络安全培训学员集合")
    private List<ApplyAllowanceStudentAddRequest> applyAllowanceStudentAddRequests;

}

