package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import cn.qbs.wa.train.allowance.entity.StatCourseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 课程资助记录(StatCourseRecord)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-19 13:56:54
 */
public interface StatCourseRecordMapper extends BaseMapper<StatCourseRecord> {

    List<StatAllowanceMonthly> summaryOrgMonthly();

}

