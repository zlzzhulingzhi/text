package cn.qbs.wa.train.allowance.service.impl;

public interface StatAllowanceRefreshService {
    /**
     * 全量刷新统计数据
     */
    Boolean refreshAll();

    /**
     * 统计月度数据
     */
    void refreshMonthlyData();

    /**
     * 统计年度数据
     */
    void refreshYearlyData();
}
