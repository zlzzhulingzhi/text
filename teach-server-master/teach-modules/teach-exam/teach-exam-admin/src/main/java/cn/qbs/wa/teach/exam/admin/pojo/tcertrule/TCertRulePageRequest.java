package cn.qbs.wa.teach.exam.admin.pojo.tcertrule;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 证书规则表(TCertRule)分页查询参数
 *
 * @author makejava
 * @since 2022-05-16 13:49:21
 */
@Data
public class TCertRulePageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "证书id")
    private Long certId;
    
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    
    @ApiModelProperty(value = "规则值")
    private String ruleValue;

}

