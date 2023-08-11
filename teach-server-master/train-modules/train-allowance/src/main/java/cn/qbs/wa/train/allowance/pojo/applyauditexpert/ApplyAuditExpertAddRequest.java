package cn.qbs.wa.train.allowance.pojo.applyauditexpert;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 资助评审专家(ApplyAuditExpert)创建资助评审专家参数
 *
 * @author makejava
 * @since 2023-04-04 13:35:41
 */
@Data
public class ApplyAuditExpertAddRequest {
    
    @ApiModelProperty(value = "申请ID")
    private Long applyId;
    
    @ApiModelProperty(value = "申请类型(申请类型(qualification-资助资格 allowance-资助资金)")
    private String applyType;
    
    @ApiModelProperty(value = "专家ID")
    private Long expertId;
    
    @ApiModelProperty(value = "评审费用(单位元)")
    private BigDecimal cost;
    
    @ApiModelProperty(value = "评审意见")
    private String comment;

}

