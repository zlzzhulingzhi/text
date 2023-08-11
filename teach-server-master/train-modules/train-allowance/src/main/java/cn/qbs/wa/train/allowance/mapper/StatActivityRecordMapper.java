package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.StatActivityRecord;
import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 活动资助记录(StatActivityRecord)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-19 13:56:53
 */
public interface StatActivityRecordMapper extends BaseMapper<StatActivityRecord> {

    List<StatAllowanceMonthly> summaryOrgMonthly();
}

