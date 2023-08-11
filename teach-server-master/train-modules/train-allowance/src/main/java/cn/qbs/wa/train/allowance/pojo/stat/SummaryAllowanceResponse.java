package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SummaryAllowanceResponse {

    @ApiModelProperty(value = "总学时")
    private Integer totalLessonNum;

    @ApiModelProperty(value = "资助人次")
    private Integer totalStudentNum;

    @ApiModelProperty(value = "资助合计(元)")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "场所费用(元)")
    private BigDecimal siteFundFee;

    @ApiModelProperty(value = "就餐费用(元)")
    private BigDecimal mealFundFee;

    @ApiModelProperty(value = "住宿费用(元)")
    private BigDecimal roomFundFee;

    @ApiModelProperty(value = "交通费用(元)")
    private BigDecimal trafficFundFee;

    @ApiModelProperty(value = "学费金额(元)")
    private BigDecimal studyFundFee;

}
