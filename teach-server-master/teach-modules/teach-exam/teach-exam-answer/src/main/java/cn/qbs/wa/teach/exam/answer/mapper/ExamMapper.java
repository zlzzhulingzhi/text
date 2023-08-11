package cn.qbs.wa.teach.exam.answer.mapper;

import cn.qbs.wa.teach.exam.answer.pojo.ExamVO;
import cn.qbs.wa.teach.exam.answer.pojo.center.*;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试表(Exam)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ExamMapper extends BaseMapper<Exam> {

    Exam selectExamByExamineeRecordId(@Param("examineeRecordId") Long examineeRecordId);

    ExamVO selectExamById(@Param("examId") Long examId);

    Boolean selectHasRule(@Param("examId") Long examId, @Param("ruleCode") String ruleCode);

    IPage<PageResponse> centerPage(IPage<?> page, @Param("params") PageRequest params);

    List<ExamineRecordResponse> calculatedExamRecordByExamIds(@Param("userId") Long userId, @Param("examIds") List<Long> examIds);

    List<ExamineRecordResponse> calculatedExamRecordByNeeId(@Param("examineeId") Long examineeId);

    IPage<PageResponse> recordPage(IPage<?> page, @Param("params") PageRequest params);
}

