package cn.qbs.wa.teach.exam.admin.service;

import java.time.LocalDateTime;

/**
 *
 * @Author zcm
 * @Date 2022-01-06 10:12
 * @Version 1.0
 */
public interface JobService {

    void addTimedOnShelfExamTask(Long examId, LocalDateTime releaseTime);

    void addTimedStartExamTask(Long examId, LocalDateTime startTime);

    void addTimedEndExamTask(Long examId, LocalDateTime localDateTime);

    void addTimedOffShelfExamTask(Long examId, LocalDateTime endTime);

}
