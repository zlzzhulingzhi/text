package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClazzAllowancePageRequest extends StatAllowancePageRequest {

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty("最小费用")
    private BigDecimal minFee;

    @ApiModelProperty("最大费用")
    private BigDecimal maxFee;
}
