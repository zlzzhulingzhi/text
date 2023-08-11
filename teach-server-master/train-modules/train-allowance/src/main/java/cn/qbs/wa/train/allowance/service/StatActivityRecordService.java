package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.StatActivityRecord;
import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 活动资助记录(StatActivityRecord)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-19 13:56:53
 */
public interface StatActivityRecordService extends IService<StatActivityRecord> {

    List<StatAllowanceMonthly> summaryOrgMonthly();
}

