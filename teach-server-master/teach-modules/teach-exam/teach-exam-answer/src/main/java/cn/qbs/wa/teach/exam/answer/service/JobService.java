package cn.qbs.wa.teach.exam.answer.service;

import java.time.LocalDateTime;

/**
 * @Author zcm
 * @Date 2022/1/4 10:27
 * @Version 1.0
 */
public interface JobService {

    void createAutoSubmitPaperJob(Long examineeRecordId, LocalDateTime localDateTime, Long orgId);

}
