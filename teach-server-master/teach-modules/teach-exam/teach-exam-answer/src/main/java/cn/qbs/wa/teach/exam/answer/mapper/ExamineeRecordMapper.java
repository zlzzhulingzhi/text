package cn.qbs.wa.teach.exam.answer.mapper;

import cn.qbs.wa.teach.exam.answer.pojo.ExamResult;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考生考试记录表(ExamineeRecord)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeRecordMapper extends BaseMapper<ExamineeRecord> {

    ExamResult selectExamResult(@Param("examineeRecordId") Long examineeRecordId);

    ExamineeRecord selectByExamineeQuestionId(@Param("examineeQuestionId") Long examineeQuestionId);

    List<ExamRecord> selectInfoByExamId(ExamPageRequest examPageRequest);

    List<ExamineeRecord> selectByExamId(@Param("examId") Long examId, @Param("examineeRecordId") Long examineeRecordId);

}

