package cn.qbs.wa.train.allowance.pojo.applyallowancefee;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资助资金申请明细-网络安全培训费用(ApplyAllowanceFee)更新资助资金申请明细-网络安全培训费用参数
 *
 * @author makejava
 * @since 2022-11-03 19:43:33
 */
@Data
public class ApplyAllowanceFeeUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "资助资金申请ID")
    private Long applyAllowanceId;

    @ApiModelProperty(value = "资助项目编号")
    private String itemCode;

    @ApiModelProperty(value = "资助项目名称")
    private String itemName;

    @ApiModelProperty(value = "资助费用(元)")
    private BigDecimal allowanceFee;

}

