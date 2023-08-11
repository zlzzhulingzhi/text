package cn.qbs.wa.teach.question.mapper;

import cn.qbs.wa.teach.question.entity.QuestionImportRecord;
import cn.qbs.wa.teach.question.pojo.question.QuestionImportRecordPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

/**
 * 试题导入记录表(QuestionImportRecord)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-17 17:41:44
 */
public interface QuestionImportRecordMapper extends BaseMapper<QuestionImportRecord> {

    IPage<QuestionImportRecordPageResponse> page(@Param("page") IPage<?> page);

}

