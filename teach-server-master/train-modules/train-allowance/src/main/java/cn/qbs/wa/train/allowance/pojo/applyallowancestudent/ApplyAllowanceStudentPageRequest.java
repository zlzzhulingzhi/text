package cn.qbs.wa.train.allowance.pojo.applyallowancestudent;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资助资金申请明细-网络安全培训学员(ApplyAllowanceStudent)分页查询参数
 *
 * @author makejava
 * @since 2022-11-03 19:30:25
 */
@Data
public class ApplyAllowanceStudentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "资助资金申请ID")
    private Long applyAllowanceId;

    @ApiModelProperty(value = "学员ID")
    private Long studentId;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

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

}

