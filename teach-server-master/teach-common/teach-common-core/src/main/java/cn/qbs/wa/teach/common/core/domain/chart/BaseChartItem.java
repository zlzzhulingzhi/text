package cn.qbs.wa.teach.common.core.domain.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据项
 * @author yujunxin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseChartItem {

    /**
     * 代号
     */
    private String key;

    /**
     * 显示名
     */
    private String name;

}
