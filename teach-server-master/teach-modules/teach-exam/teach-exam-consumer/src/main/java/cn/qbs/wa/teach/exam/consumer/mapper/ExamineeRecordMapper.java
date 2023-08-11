package cn.qbs.wa.teach.exam.consumer.mapper;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 考生考试记录表(ExamineeRecord)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeRecordMapper extends BaseMapper<ExamineeRecord> {

    BigDecimal selectScore(@Param("examineeRecordId") Long examineeRecordId);

    ExamineeRecord selectByExamineeRecordQuestionId(@Param("examineeRecordQuestionId") Long examineeRecordQuestionId);

}

