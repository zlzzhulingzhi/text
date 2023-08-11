package cn.qbs.wa.teach.common.core.domain.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数据项
 * @author yujunxin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChartItem extends BaseChartItem {

    public ChartItem(String key, String name, List<Number> values) {
        super(key, name);
        this.series = values;
    }

    /**
     * Y轴显示值(每一项对应一个X轴数据)
     */
    private List<Number> series;
}
