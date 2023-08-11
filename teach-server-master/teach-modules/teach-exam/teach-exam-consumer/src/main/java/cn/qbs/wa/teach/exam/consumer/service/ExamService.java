package cn.qbs.wa.teach.exam.consumer.service;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考试表(Exam)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ExamService extends IService<Exam> {

    void addExamQuestions(Long examId, Long examineeId, Long orgId, Long userId);

    ExamAndCertDetailResponse selectCertRuleByExamineeId(Long examineeId);
}

