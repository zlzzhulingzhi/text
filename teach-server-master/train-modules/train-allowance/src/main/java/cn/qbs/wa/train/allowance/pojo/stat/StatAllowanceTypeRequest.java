package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatAllowanceTypeRequest {

    @ApiModelProperty("资质类型 allowance-课程申请 activity-活动申请")
    private String applyType;
}
