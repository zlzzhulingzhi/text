package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import cn.qbs.wa.train.allowance.entity.StatCourseRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 课程资助记录(StatCourseRecord)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-19 13:56:54
 */
public interface StatCourseRecordService extends IService<StatCourseRecord> {

    List<StatAllowanceMonthly> summaryOrgMonthly();

}

