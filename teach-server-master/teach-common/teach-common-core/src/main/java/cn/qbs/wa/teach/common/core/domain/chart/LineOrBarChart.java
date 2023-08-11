package cn.qbs.wa.teach.common.core.domain.chart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 条形图、折线图
 * @author yujunxin
 */
@Data
public class LineOrBarChart {

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String chartName;

    /**
     * x-轴数据
     */
    @ApiModelProperty("x轴数据")
    private List<String> xAxis;

    /**
     * 每一个X轴对应的数据项
     */
    @ApiModelProperty("数据项")
    private List<ChartItem> chartItems;

}
