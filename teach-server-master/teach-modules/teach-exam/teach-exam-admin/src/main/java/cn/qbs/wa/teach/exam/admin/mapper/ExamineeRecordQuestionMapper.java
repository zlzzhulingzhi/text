package cn.qbs.wa.teach.exam.admin.mapper;

import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRecordDetails;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamRecordQuestionRequest;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 考生答题记录表(ExamineeRecordQuestion)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
public interface ExamineeRecordQuestionMapper extends BaseMapper<ExamineeRecordQuestion> {

    void mark(ExamRecordQuestionRequest request);
}

