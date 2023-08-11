package cn.qbs.wa.teach.question.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.question.pojo.paper.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.question.entity.Paper;

/**
 * 试卷(Paper)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:48:34
 */
public interface PaperMapper extends BaseMapper<Paper> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Paper> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Paper> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Paper> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Paper> entities);

    IPage<PaperPageResponse> page(@Param("page") IPage<?> page, @Param("params") PaperPageRequest params);

    PaperDetailResponse selectDetailById(Serializable id);

    List<Paper> selectListByQuestionId(@Param("questionId") Long questionId);

    @InterceptorIgnore(tenantLine = "true")
    List<Paper> selectPaper(@Param("paperIdList") List<Long> paperIdList, @Param("size") int size);

    IPage<Long> pageId(@Param("page") Page<?> page, @Param("param") PaperSearchRequest param);

    List<PaperSearchResult> selectByIdListAndOrder(@Param("paperIdList") List<Long> paperIdList);

}

