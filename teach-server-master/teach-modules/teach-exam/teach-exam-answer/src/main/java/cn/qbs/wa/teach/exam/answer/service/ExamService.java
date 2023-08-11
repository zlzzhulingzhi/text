package cn.qbs.wa.teach.exam.answer.service;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.answer.pojo.ExamVO;
import cn.qbs.wa.teach.exam.answer.pojo.ExamWithQuestions;
import cn.qbs.wa.teach.exam.answer.pojo.SubmitAnswerRequest;
import cn.qbs.wa.teach.exam.answer.pojo.center.PageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.center.PageResponse;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamAnswerSubmitRequest;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 考试表(Exam)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ExamService extends IService<Exam> {

    ExamVO getExam(Long examId);

    R<ExamWithQuestions> getExamWithQuestions(Long examId);

    Examinee selectOrAddExaminee(Exam exam, Long orgId, Long userId);

    void updateStartAnswerTime(Long examineeRecordId);

    void submitAnswer(List<SubmitAnswerRequest> submitAnswerList);

    void submitAnswer(ExamAnswerSubmitRequest submitAnswer);

    void submitPaper(Long examineeRecordId);

    void submitPaper(ExamineeRecord examineeRecord);

    /**
     * 再次考试
     * @param examId
     */
    void againExam(Long examId);

    /**
     * 考试中心
     * @param params 请求参数
     * @return 考试列表
     */
    IPage<PageResponse> centerPage(PageRequest params);

    IPage<PageResponse> recordPage(PageRequest params);
}
