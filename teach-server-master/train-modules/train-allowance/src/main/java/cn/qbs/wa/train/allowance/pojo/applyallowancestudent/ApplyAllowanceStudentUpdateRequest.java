package cn.qbs.wa.train.allowance.pojo.applyallowancestudent;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资助资金申请明细-网络安全培训学员(ApplyAllowanceStudent)更新资助资金申请明细-网络安全培训学员参数
 *
 * @author makejava
 * @since 2022-11-03 19:30:26
 */
@Data
public class ApplyAllowanceStudentUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "资助资金申请ID")
    private Long applyAllowanceId;

    @ApiModelProperty(value = "学员ID")
    private Long studentId;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "学员用户ID")
    private Long memberId;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "学时")
    private Integer lessonNum;

    @ApiModelProperty(value = "缴费金额(元)")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "补助金额(元)")
    private BigDecimal supplyAmount;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal examScore;

    @ApiModelProperty(value = "考试结果")
    private String examResult;

}

