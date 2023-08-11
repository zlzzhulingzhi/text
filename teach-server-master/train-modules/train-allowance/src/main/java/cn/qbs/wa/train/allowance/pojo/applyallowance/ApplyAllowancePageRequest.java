package cn.qbs.wa.train.allowance.pojo.applyallowance;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)分页查询参数
 *
 * @author makejava
 * @since 2022-11-03 19:28:57
 */
@Data
public class ApplyAllowancePageRequest extends BasePageRequest {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "培训结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "考试人数")
    private Integer examNum;

    @ApiModelProperty(value = "考试日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime examDate;

    @ApiModelProperty(value = "班级ID")
    private Long classId;

    @ApiModelProperty(value = "班级名称")
    private String className;

}

