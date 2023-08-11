package cn.qbs.wa.teach.question.mapper;

import cn.qbs.wa.teach.question.entity.QuestionType;
import cn.qbs.wa.teach.question.pojo.question.type.QuestionTypeDetailResponse;
import cn.qbs.wa.teach.question.pojo.question.type.QuestionTypePageRequest;
import cn.qbs.wa.teach.question.pojo.question.type.QuestionTypePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 题型(QuestionType)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 13:35:24
 */
public interface QuestionTypeMapper extends BaseMapper<QuestionType> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<QuestionType> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<QuestionType> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<QuestionType> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<QuestionType> entities);

    IPage<QuestionTypePageResponse> page(@Param("page") IPage<?> page, @Param("params") QuestionTypePageRequest params);

    QuestionTypeDetailResponse selectDetailById(Serializable id);

}

