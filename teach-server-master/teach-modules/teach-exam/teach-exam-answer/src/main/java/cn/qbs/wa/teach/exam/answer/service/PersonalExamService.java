package cn.qbs.wa.teach.exam.answer.service;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.answer.pojo.ExamVO;
import cn.qbs.wa.teach.exam.answer.pojo.ExamWithQuestions;
import cn.qbs.wa.teach.exam.answer.pojo.center.ExamCommonRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamAnswerSubmitRequest;

/**
 * 考试表(Exam)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface PersonalExamService {

    /**
     * 获取考试信息
     * @param examId 考试ID
     * @return 考试须知
     */
    ExamVO getExam(Long examId);

    /**
     * 获取试题
     * @param request 参数
     * @return 试题内容
     */
    R<ExamWithQuestions> getExamWithQuestions(ExamCommonRequest request);

    /**
     * 提交答案
     * @param submitAnswer 参数
     */
    void submitAnswer(ExamAnswerSubmitRequest submitAnswer);

    /**
     * 重新创建考试记录和试题
     * @param examineeId 考试ID
     * @param lastExamRecordId 旧考试记录ID
     * @return 新考试记录ID
     */
    Long reCreateExamRecord(Long examineeId, Long lastExamRecordId);

    /**
     * 预览考试
     * @param examId 考试ID
     * @return 考试须知
     */
    ExamVO examPreview(Long examId);
}
