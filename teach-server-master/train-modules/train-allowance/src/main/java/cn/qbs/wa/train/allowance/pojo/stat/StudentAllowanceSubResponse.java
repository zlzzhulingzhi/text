package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentAllowanceSubResponse {

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "课程名称")
    private String projectName;

    @ApiModelProperty(value = "学时")
    private Integer lessonNum;

    @ApiModelProperty(value = "缴费金额(元)")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "补助金额(元)")
    private BigDecimal supplyAmount;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal examScore;
}
