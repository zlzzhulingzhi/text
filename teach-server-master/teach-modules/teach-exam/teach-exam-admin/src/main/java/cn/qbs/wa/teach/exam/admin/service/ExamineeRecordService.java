package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考生考试记录表(ExamineeRecord)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeRecordService extends IService<ExamineeRecord> {

    IPage<ExamRecord> selectListByExamId(ExamRecordPageRequest params);

    IPage<ExamRanking> rankingByExamId(ExamRecordPageRequest params);

    QuestionCorrectRateWrap questionCorrectRateByExamId(Long examId);

    QuestionAnalysisWrap questionAnalysisByExamId(Long examId);

    ExamRecordDetails recordDetails(Long examineeRecordId);


    IPage<ExamPageResponse> page(ExamPageRequest params);

    boolean deleteRecord(Long examineeRecordId);

    void updateCalculated(Long examineeId);

    Long selectCountByExamineeId(Long examineeId);

}

