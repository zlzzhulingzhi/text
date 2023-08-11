package cn.qbs.wa.teach.exam.consumer.service;

/**
 *
 * @Author zcm
 * @Date 2021-12-22 17:52
 * @Version 1.0
 */
public interface CorrectService {

    void correct(Long examineeRecordId);

    void afterManualCorrect(Long examineeRecordQuestionId, Long orgId);

}
