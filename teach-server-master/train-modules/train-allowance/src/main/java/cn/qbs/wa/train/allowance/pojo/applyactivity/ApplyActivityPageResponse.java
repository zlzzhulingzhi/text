package cn.qbs.wa.train.allowance.pojo.applyactivity;

import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)分页查询资助资金申请-学术会议和竞赛活动响应
 *
 * @author makejava
 * @since 2022-11-03 19:27:16
 */
@Data
public class ApplyActivityPageResponse extends ApplyPageResponse {

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动主题")
    private String activityTheme;

    @ApiModelProperty(value = " 活动预算金额(元)")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "申请人")
    private String applyUserName;

}

