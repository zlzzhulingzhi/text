package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author makejava
 * @since 2022-11-14 15:35:53
 */
@Data
public class FeeResponse {

    @ApiModelProperty(value = "活动资助费用合计(元)")
    private BigDecimal totalActivityFee;

    @ApiModelProperty(value = "课程资助费用合计(元)")
    private BigDecimal totalCourseFee;

    @ApiModelProperty(value = "培训场所资助费用(元)")
    private BigDecimal siteFundFee;

    @ApiModelProperty(value = "学员就餐资助费用(元)")
    private BigDecimal mealFundFee;

    @ApiModelProperty(value = "学员住宿资助费用(元)")
    private BigDecimal roomFundFee;

    @ApiModelProperty(value = "学员交通资助费用(元)")
    private BigDecimal trafficFundFee;

    @ApiModelProperty(value = "学员学费资助金额(元)")
    private BigDecimal studyFundFee;

}
