package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.train.allowance.entity.StatActivityRecord;
import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import cn.qbs.wa.train.allowance.mapper.StatActivityRecordMapper;
import cn.qbs.wa.train.allowance.service.StatActivityRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动资助记录(StatActivityRecord)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-19 13:56:53
 */
@Slf4j
@Service("statActivityRecordService")
public class StatActivityRecordServiceImpl extends ServiceImpl<StatActivityRecordMapper, StatActivityRecord> implements StatActivityRecordService {
    @Override
    public List<StatAllowanceMonthly> summaryOrgMonthly() {
        return this.baseMapper.summaryOrgMonthly();
    }
}

