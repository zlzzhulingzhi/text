package cn.qbs.wa.teach.exam.answer;

import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.job.util.EventXxlJobUtil;
import cn.qbs.wa.teach.exam.answer.job.JobNames;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zcm
 * @Date 2022/1/4 15:44
 * @Version 1.0
 */
@Slf4j
@SpringBootTest
public class JobTest {

    @Autowired
    private EventXxlJobUtil eventXxlJobUtil;


    @Test
    public void createJob () {
        long eventId = IdUtil.getSnowflake().nextId();
        LocalDateTime examStartTime = LocalDateTime.now();
        LocalDateTime lastSubmitPaperLocalDateTime = examStartTime.plusMinutes(2);
        Date executorDate = Date.from(lastSubmitPaperLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> map = new HashMap<>(2);
        map.put("examineeRecordId", 9L);
        map.put("orgId", 37L);
        eventXxlJobUtil.addExecutorTask(eventId, JobNames.AUTO_SUBMIT_PAPER, map, executorDate);
    }

}
