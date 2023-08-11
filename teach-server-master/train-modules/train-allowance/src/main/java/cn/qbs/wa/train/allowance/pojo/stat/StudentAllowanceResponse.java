package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentAllowanceResponse {

    @ApiModelProperty(value = "学员ID")
    private Long memberId;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "学员手机号")
    private String studentPhone;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "学时")
    private Integer lessonNum;

    @ApiModelProperty(value = "缴费金额(元)")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "补助金额(元)")
    private BigDecimal supplyAmount;
}
