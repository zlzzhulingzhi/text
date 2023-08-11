package cn.qbs.wa.teach.question.mapper;

import cn.qbs.wa.teach.question.entity.PaperQuestionType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷题型(PaperQuestionType)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-19 15:18:23
 */
public interface PaperQuestionTypeMapper extends BaseMapper<PaperQuestionType> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<PaperQuestionType> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<PaperQuestionType> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<PaperQuestionType> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<PaperQuestionType> entities);
    
}

