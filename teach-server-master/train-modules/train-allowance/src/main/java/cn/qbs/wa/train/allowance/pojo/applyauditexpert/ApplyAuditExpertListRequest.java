package cn.qbs.wa.train.allowance.pojo.applyauditexpert;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资助评审专家(ApplyAuditExpert)更新资助评审专家参数
 *
 * @author makejava
 * @since 2023-04-04 14:19:13
 */
@Data
public class ApplyAuditExpertListRequest {
    
    @ApiModelProperty(value = "申请id")
    private Long id;
    
    @ApiModelProperty(value = "申请类型(申请类型(qualification-资助资格 allowance-资助资金)")
    private String applyType;


}

