package cn.qbs.wa.teach.exam.consumer.mapper;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 考试表(Exam)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ExamMapper extends BaseMapper<Exam> {

    Boolean selectHasRule(@Param("examId") Long examId, @Param("ruleCode") String ruleCode);

    ExamAndCertDetailResponse selectCertRuleByExamineeId(Long examineeId);
}

