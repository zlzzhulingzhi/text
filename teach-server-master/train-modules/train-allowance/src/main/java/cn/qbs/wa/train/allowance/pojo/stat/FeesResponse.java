package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author makejava
 * @since 2022-11-14 15:35:53
 */
@Data
public class FeesResponse {

    @ApiModelProperty(value = "资助费用合计(元)")
    private List<BigDecimal> totalFeeList;

    @ApiModelProperty(value = "活动资助费用合计(元)")
    private List<BigDecimal> totalActivityFeeList;

    @ApiModelProperty(value = "课程资助费用合计(元)")
    private List<BigDecimal> totalCourseFeeList;

    @ApiModelProperty(value = "培训场所资助费用(元)")
    private List<BigDecimal> siteFundFeeList;

    @ApiModelProperty(value = "学员就餐资助费用(元)")
    private List<BigDecimal> mealFundFeeList;

    @ApiModelProperty(value = "学员住宿资助费用(元)")
    private List<BigDecimal> roomFundFeeList;

    @ApiModelProperty(value = "学员交通资助费用(元)")
    private List<BigDecimal> trafficFundFeeList;

    @ApiModelProperty(value = "学员学费资助金额(元)")
    private List<BigDecimal> studyFundFeeList;

}
