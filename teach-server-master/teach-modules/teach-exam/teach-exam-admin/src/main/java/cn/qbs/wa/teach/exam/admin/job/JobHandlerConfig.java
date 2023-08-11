package cn.qbs.wa.teach.exam.admin.job;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.exam.admin.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.admin.service.ComExamService;
import cn.qbs.wa.teach.exam.admin.service.ExamService;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.enumerate.ExamStatusEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcm
 * @Date 2022/1/4 10:37
 * @Version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobHandlerConfig {

    private final ExamService examService;

    @Resource(name = "ComExamServiceImpl")
    private ComExamService comExamService;

    @Resource
    private ExamMapper examMapper;


    /**
     * 考试定时上架
     */
    @XxlJob(JobNames.EXAM_TIMED_ON_SHELF)
    public void examTimedOnShelf() {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("考试定时上架任务执行，参数：{}", params);
        if (StringUtils.isEmpty(params)) {
            XxlJobHelper.log("参数为空，执行失败！");
            XxlJobHelper.handleFail();
            return;
        }

        JSONObject jsonObject = JSON.parseObject(params);
        Long examId = jsonObject.getLong("examId");
        Long orgId = jsonObject.getLong("orgId");
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
        comExamService.release(examId, false);
    }

    /**
     * 考试定时下架
     */
    @XxlJob(JobNames.EXAM_TIMED_OFF_SHELF)
    public void examTimedOffShelf() {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("考试定时下架任务执行，参数：{}", params);
        if (StringUtils.isEmpty(params)) {
            XxlJobHelper.log("参数为空，执行失败！");
            XxlJobHelper.handleFail();
            return;
        }

        JSONObject jsonObject = JSON.parseObject(params);
        Long examId = jsonObject.getLong("examId");
        Long orgId = jsonObject.getLong("orgId");
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
        examService.offShelf(examId, false);
    }

    /**
     * 考试定时开始
     */
    @XxlJob(JobNames.EXAM_TIMED_START)
    public void examTimedStart() {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("考试定时开始任务执行，参数：{}", params);
        if (StringUtils.isEmpty(params)) {
            XxlJobHelper.log("参数为空，执行失败！");
            XxlJobHelper.handleFail();
            return;
        }

        JSONObject jsonObject = JSON.parseObject(params);
        Long examId = jsonObject.getLong("examId");
        Long orgId = jsonObject.getLong("orgId");
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
        comExamService.start(examId, false);
    }

    /**
     * 考试定时结束
     */
    @XxlJob(JobNames.EXAM_TIMED_END)
    public void examTimedEnd() {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("考试定时停止任务执行，参数：{}", params);
        log.info("考试定时停止任务执行，参数：{}", params);
        if (StringUtils.isEmpty(params)) {
            XxlJobHelper.log("参数为空，执行失败！");
            XxlJobHelper.handleFail();
            return;
        }

        JSONObject jsonObject = JSON.parseObject(params);
        Long examId = jsonObject.getLong("examId");
        Long orgId = jsonObject.getLong("orgId");
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
        comExamService.end(examId, false);
    }



    /**
     * 检查考试定时开始
     */
    @XxlJob(JobNames.CHECK_EXAM_TIMED_START)
    public void checkExamTimedStart() {
        XxlJobHelper.log("检查考试定时开始任务执行...");
        Instant beginTime = Instant.now();

        List<Exam> examList = examMapper.selectTimeoutNotStarted(ExamStatusEnum.DURING_EXAM.getStatus(), 100);
        List<Long> examIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examList)) {
            examIdList = examList.stream().map(Exam::getId).collect(Collectors.toList());
            for (Exam exam : examList) {
                Long examId = exam.getId();
                Long orgId = exam.getOrgId();
                SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
                comExamService.start(examId, false);
            }
        }

        XxlJobHelper.log("检查考试定时开始任务执行完毕，数量：{}，耗时{}秒，考试ID列表：{}", examIdList.size(), Duration.between(beginTime, Instant.now()).getSeconds(), examIdList);
    }

    /**
     * 检查考试定时结束
     */
    @XxlJob(JobNames.CHECK_EXAM_TIMED_END)
    public void checkExamTimedEnd() {
        XxlJobHelper.log("检查考试定时结束任务执行...");
        Instant beginTime = Instant.now();

        List<Exam> examList = examMapper.selectTimeoutNotEnded(ExamStatusEnum.EXAM_ENDED.getStatus(), 100);
        List<Long> examIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(examList)) {
            examIdList = examList.stream().map(Exam::getId).collect(Collectors.toList());
            for (Exam exam : examList) {
                Long examId = exam.getId();
                Long orgId = exam.getOrgId();
                SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
                examService.end(examId);
            }
        }

        XxlJobHelper.log("检查考试定时开始任务执行完毕，数量：{}，耗时{}秒，考试ID列表：{}", examIdList.size(), Duration.between(beginTime, Instant.now()).getSeconds(), examIdList);
    }

}
