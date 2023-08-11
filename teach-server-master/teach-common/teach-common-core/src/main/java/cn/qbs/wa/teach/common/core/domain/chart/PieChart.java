package cn.qbs.wa.teach.common.core.domain.chart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 饼图
 * @author yujunxin
 */
@Data
public class PieChart {

    /**
     * 饼图名称
     */
    @ApiModelProperty("名称")
    private String chartName;

    /**
     * 饼图数据项
     */
    @ApiModelProperty("数据项")
    private List<PieChartItem> chartItems;

}
