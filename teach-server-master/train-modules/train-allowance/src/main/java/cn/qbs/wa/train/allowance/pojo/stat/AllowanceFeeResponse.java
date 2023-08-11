package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AllowanceFeeResponse {

    @ApiModelProperty(value = "资助人次")
    private String itemCode;

    @ApiModelProperty(value = "课程各类费用(元)")
    private BigDecimal totalAllowanceFee;

}
