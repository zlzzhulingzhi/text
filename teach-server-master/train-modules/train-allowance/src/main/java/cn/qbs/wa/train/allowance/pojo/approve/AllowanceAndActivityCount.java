package cn.qbs.wa.train.allowance.pojo.approve;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AllowanceAndActivityCount {

    @ApiModelProperty(value = "课程和活动未审核数")
    private Long allowanceAndActivityCount;

    @ApiModelProperty(value = "课程未审核数")
    private Long allowanceCount;

    @ApiModelProperty(value = "活动未审核数")
    private Long activityCount;

}
