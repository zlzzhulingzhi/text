package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import cn.qbs.wa.train.allowance.entity.StatCourseRecord;
import cn.qbs.wa.train.allowance.mapper.StatCourseRecordMapper;
import cn.qbs.wa.train.allowance.service.StatCourseRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程资助记录(StatCourseRecord)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-19 13:56:54
 */
@Slf4j
@Service("statCourseRecordService")
public class StatCourseRecordServiceImpl extends ServiceImpl<StatCourseRecordMapper, StatCourseRecord> implements StatCourseRecordService {
    @Override
    public List<StatAllowanceMonthly> summaryOrgMonthly() {
        return this.baseMapper.summaryOrgMonthly();
    }
}

