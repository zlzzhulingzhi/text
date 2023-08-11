package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.job.util.EventXxlJobUtil;
import cn.qbs.wa.teach.exam.admin.job.JobNames;
import cn.qbs.wa.teach.exam.admin.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Author zcm
 * @Date 2022-01-06 10:12
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final EventXxlJobUtil eventXxlJobUtil;


    @Override
    public void addTimedOnShelfExamTask(Long examId, LocalDateTime releaseTime) {
        if (examId != null && releaseTime != null) {
            long eventId = IdUtil.getSnowflake().nextId();
            Date executorDate = Date.from(releaseTime.atZone(ZoneId.systemDefault()).toInstant());
            Map<String, Object> params = new HashMap<>(2);
            params.put("examId", examId);
            params.put("orgId", SecurityContextHolder.getOrgId());
            eventXxlJobUtil.addExecutorTask(eventId, JobNames.EXAM_TIMED_ON_SHELF, params, executorDate);
        }
    }

    @Override
    public void addTimedStartExamTask(Long examId, LocalDateTime startTime) {
        if (examId != null && startTime != null) {
            long eventId = IdUtil.getSnowflake().nextId();
            Date executorDate = Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant());
            Map<String, Object> params = new HashMap<>(2);
            params.put("examId", examId);
            params.put("orgId", SecurityContextHolder.getOrgId());
            eventXxlJobUtil.addExecutorTask(eventId, JobNames.EXAM_TIMED_START, params, executorDate);
        }
    }

    @Override
    public void addTimedEndExamTask(Long examId, LocalDateTime localDateTime) {
        if (examId != null && localDateTime != null) {
            long eventId = IdUtil.getSnowflake().nextId();
            Date executorDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Map<String, Object> params = new HashMap<>(2);
            params.put("examId", examId);
            params.put("orgId", SecurityContextHolder.getOrgId());
            eventXxlJobUtil.addExecutorTask(eventId, JobNames.EXAM_TIMED_END, params, executorDate);
        }
    }

    @Override
    public void addTimedOffShelfExamTask(Long examId, LocalDateTime endTime) {
        if (examId != null && endTime != null) {
            long eventId = IdUtil.getSnowflake().nextId();
            Date executorDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
            Map<String, Object> params = new HashMap<>(2);
            params.put("examId", examId);
            params.put("orgId", SecurityContextHolder.getOrgId());
            eventXxlJobUtil.addExecutorTask(eventId, JobNames.EXAM_TIMED_OFF_SHELF, params, executorDate);
        }
    }

}
