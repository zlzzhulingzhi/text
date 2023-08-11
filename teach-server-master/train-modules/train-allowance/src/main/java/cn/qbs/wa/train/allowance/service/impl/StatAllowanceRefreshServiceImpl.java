package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.qbs.wa.train.allowance.entity.StatActivityRecord;
import cn.qbs.wa.train.allowance.entity.StatAllowanceMonthly;
import cn.qbs.wa.train.allowance.entity.StatCourseRecord;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.mapper.ApplyActivityMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyAllowanceMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyMapper;
import cn.qbs.wa.train.allowance.mapper.StatAllowanceYearlyMapper;
import cn.qbs.wa.train.allowance.service.StatActivityRecordService;
import cn.qbs.wa.train.allowance.service.StatAllowanceMonthlyService;
import cn.qbs.wa.train.allowance.service.StatCourseRecordService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("statAllowanceRefreshService")
public class StatAllowanceRefreshServiceImpl implements StatAllowanceRefreshService {

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private ApplyAllowanceMapper allowanceMapper;

    @Resource
    private ApplyActivityMapper activityMapper;

    @Resource
    private StatAllowanceYearlyMapper allowanceYearlyMapper;

    @Resource
    private StatCourseRecordService courseRecordService;

    @Resource
    private StatActivityRecordService activityRecordService;

    @Resource
    private StatAllowanceMonthlyService allowanceMonthlyService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean refreshAll() {
        boolean isRefresh = false;
        // 获取询机构列表，分别查询机构下的申请记录进行统计
        List<Long> orgList = applyMapper.collectOrg(Collections.singletonList(ApplyTypeEnum.ALLOWANCE.getCode()));
        for (Long org : orgList) {
            isRefresh = true;
            // 收集机构资助相关申请数据
            this.collectOrgAllowanceRecord(org);
        }

        // 统计月度数据
        this.refreshMonthlyData();

        // 统计年度数据
        this.refreshYearlyData();

        return isRefresh;
    }

    @Override
    public void refreshYearlyData() {
        // 先删除旧有数据
        allowanceYearlyMapper.deleteAll();
        // 从月度统计数据记录进行重新统计
        allowanceYearlyMapper.refresh();
    }

    @Override
    public void refreshMonthlyData() {
        List<StatAllowanceMonthly> monthlyList = new ArrayList<>();

        // 根据课程资助申请记录整合数据
        List<StatAllowanceMonthly> courseList = courseRecordService.summaryOrgMonthly();
        if (CollUtil.isNotEmpty(courseList)) {
            monthlyList.addAll(courseList);
        }

        // 根据活动资助申请记录整合数据
        List<StatAllowanceMonthly> actList = activityRecordService.summaryOrgMonthly();
        if (CollUtil.isNotEmpty(actList)) {
            monthlyList.addAll(actList);
        }

        Map<String, StatAllowanceMonthly> monthlyMap = new HashMap<>(monthlyList.size());
        for (StatAllowanceMonthly monthly : monthlyList) {
            // orgId-Year-Month
            String key = String.format("%d-%d-%d", monthly.getOrgId(), monthly.getYear(), monthly.getMonth());
            // 同年同月的数据课程和活动资助数据合并
            if (monthlyMap.containsKey(key)) {
                StatAllowanceMonthly allowanceMonthly = monthlyMap.get(key);
                if (monthly.getTotalStudentNum() != null) {
                    allowanceMonthly.setTotalStudentNum(monthly.getTotalStudentNum() + Optional.ofNullable(allowanceMonthly.getTotalStudentNum()).orElse(0));
                }
                if (monthly.getTotalLessonNum() != null) {
                    allowanceMonthly.setTotalLessonNum(monthly.getTotalLessonNum() + Optional.ofNullable(allowanceMonthly.getTotalLessonNum()).orElse(0));
                }
                allowanceMonthly.setTotalActivityFee(NumberUtil.add(monthly.getTotalActivityFee(), allowanceMonthly.getTotalActivityFee()));
                allowanceMonthly.setTotalCourseFee(NumberUtil.add(monthly.getTotalCourseFee(), allowanceMonthly.getTotalCourseFee()));
                allowanceMonthly.setSiteFundFee(NumberUtil.add(monthly.getSiteFundFee(), allowanceMonthly.getSiteFundFee()));
                allowanceMonthly.setMealFundFee(NumberUtil.add(monthly.getMealFundFee(), allowanceMonthly.getMealFundFee()));
                allowanceMonthly.setRoomFundFee(NumberUtil.add(monthly.getRoomFundFee(), allowanceMonthly.getRoomFundFee()));
                allowanceMonthly.setTrafficFundFee(NumberUtil.add(monthly.getTrafficFundFee(), allowanceMonthly.getTrafficFundFee()));
                allowanceMonthly.setStudyFundFee(NumberUtil.add(monthly.getStudyFundFee(), allowanceMonthly.getStudyFundFee()));
            } else {
                monthlyMap.put(key, monthly);
            }
        }

        if (!monthlyMap.isEmpty()) {
            // 删除原有数据
            allowanceMonthlyService.remove(Wrappers.<StatAllowanceMonthly>lambdaQuery().ge(StatAllowanceMonthly::getId,1L));
            // 新增数据
            allowanceMonthlyService.saveBatch(monthlyMap.values());
        }
    }

    private void collectOrgAllowanceRecord(Long org) {
        List<StatCourseRecord> courseRecordList;
        List<StatActivityRecord> activityRecordList;
        // 获取所有审核通过的课程申请
        courseRecordList = allowanceMapper.listPassAllowanceFee(org);
        if (CollUtil.isNotEmpty(courseRecordList)) {
            // 学员课时
            List<StatCourseRecord> records = allowanceMapper.listPassAllowanceLesson(org);
            if (CollUtil.isNotEmpty(records)) {
                Map<Long, Integer> map = records.stream().collect(Collectors.toMap(StatCourseRecord::getApplyId, StatCourseRecord::getTotalLessonNum, (a, b) -> a));
                for (StatCourseRecord record : courseRecordList) {
                    record.setTotalLessonNum(map.getOrDefault(record.getApplyId(), 0));
                }
            }
        }
        // 获取所有审核通过的活动申请
        activityRecordList = activityMapper.listPassActivity(org);

        if (CollUtil.isNotEmpty(courseRecordList)) {
            // 删除所有数据
            courseRecordService.remove(Wrappers.<StatCourseRecord>lambdaQuery().ge(StatCourseRecord::getId,1L));
            courseRecordService.saveBatch(courseRecordList);
        }

        if (CollUtil.isNotEmpty(activityRecordList)) {
            // 删除所有数据
            activityRecordService.remove(Wrappers.<StatActivityRecord>lambdaQuery().ge(StatActivityRecord::getId,1L));
            activityRecordService.saveBatch(activityRecordList);
        }
    }
}
