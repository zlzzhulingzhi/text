package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.job.util.EventXxlJobUtil;
import cn.qbs.wa.teach.exam.answer.job.JobNames;
import cn.qbs.wa.teach.exam.answer.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zcm
 * @Date 2022/1/4 10:27
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final EventXxlJobUtil eventXxlJobUtil;


    @Override
    public void createAutoSubmitPaperJob(Long examineeRecordId, LocalDateTime localDateTime, Long orgId) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("examineeRecordId", examineeRecordId);
        params.put("orgId", orgId);
        long eventId = IdUtil.getSnowflake().nextId();
        Date executorDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        log.info("添加强制交卷定时任务: {examineeRecordId: {}, executorDate: {}, orgId: {}}", examineeRecordId, executorDate, orgId);
        eventXxlJobUtil.addExecutorTask(eventId, JobNames.AUTO_SUBMIT_PAPER, params, executorDate);
    }

}
