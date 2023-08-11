package cn.qbs.wa.teach.common.core.domain.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 饼图数据项
 * @author yujunxin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PieChartItem extends BaseChartItem {

    public PieChartItem(String key, String name, Number value) {
        super(key, name);
        this.value = value;
    }

    /**
     * 显示值
     */
    private Number value;
}
