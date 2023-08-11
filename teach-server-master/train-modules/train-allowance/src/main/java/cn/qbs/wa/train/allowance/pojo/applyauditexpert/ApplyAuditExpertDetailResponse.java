package cn.qbs.wa.train.allowance.pojo.applyauditexpert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.allowance.entity.ApplyAuditExpert;

/**
 * 资助评审专家(ApplyAuditExpert)资助评审专家详情
 *
 * @author makejava
 * @since 2023-04-04 13:35:41
 */
@Data
public class ApplyAuditExpertDetailResponse extends ApplyAuditExpert {

    @ApiModelProperty(value = "姓名")
    private String name;

}

