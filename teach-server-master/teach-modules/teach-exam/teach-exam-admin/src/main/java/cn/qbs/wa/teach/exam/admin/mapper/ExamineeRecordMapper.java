package cn.qbs.wa.teach.exam.admin.mapper;

import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考生考试记录表(ExamineeRecord)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeRecordMapper extends BaseMapper<ExamineeRecord> {

    IPage<ExamRecord> selectListByExamId(@Param("page") IPage<?> page, @Param("params") ExamRecordPageRequest params);

    IPage<ExamRanking> rankingByExamId(@Param("page") IPage<?> page, @Param("params") ExamRecordPageRequest params);

    List<QuestionCorrectRate> questionCorrectRateByExamId(@Param("examId") Long examId);

    List<QuestionAnalysis> questionAnalysisByExamId(@Param("examId") Long examId);

    List<ExamineeRecord> selectUnsubmitListByExamId(@Param("examId") Long examId);

    List<ExamineeRecord> selectByExamId(@Param("examId") Long examId);

}

