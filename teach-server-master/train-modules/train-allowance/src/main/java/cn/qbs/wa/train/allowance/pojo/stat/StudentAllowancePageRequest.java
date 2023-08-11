package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentAllowancePageRequest extends StatAllowancePageRequest {

    @ApiModelProperty(value = "学员ID")
    private Long memberId;

    @ApiModelProperty("学员姓名")
    private String studentName;

    @ApiModelProperty("手机号")
    private String studentPhone;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty("最小费用")
    private BigDecimal minFee;

    @ApiModelProperty("最大费用")
    private BigDecimal maxFee;
}
