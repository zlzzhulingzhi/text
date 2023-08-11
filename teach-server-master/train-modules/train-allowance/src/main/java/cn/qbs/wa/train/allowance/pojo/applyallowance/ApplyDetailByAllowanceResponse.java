package cn.qbs.wa.train.allowance.pojo.applyallowance;

import cn.qbs.wa.train.allowance.entity.Apply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyDetailByAllowanceResponse extends Apply {

    @ApiModelProperty(value = "申请课程数据")
    private ApplyAllowanceDetailResponse applyAllowanceDetailResponse;
}
