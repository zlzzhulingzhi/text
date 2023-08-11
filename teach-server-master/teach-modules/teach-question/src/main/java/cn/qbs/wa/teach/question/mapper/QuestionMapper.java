package cn.qbs.wa.teach.question.mapper;

import cn.qbs.wa.teach.question.entity.Category;
import cn.qbs.wa.teach.question.entity.Question;
import cn.qbs.wa.teach.question.pojo.question.QuestionDetailResponse;
import cn.qbs.wa.teach.question.pojo.question.search.QuestionSearchRequest;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 试题(Question)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:16:36
 */
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 分页查询，返回试题ID
     * @param page
     * @param params
     * @return
     */
    IPage<Long> pageId(@Param("page") IPage<?> page, @Param("params") QuestionSearchRequest params);

    QuestionDetailResponse selectDetailById(Serializable id);

    List<QuestionDetailResponse> selectDetailsByIdList(@Param("idList") List<Long> idList);

    List<QuestionDetailResponse> selectDetailsByIdListByOrder(@Param("idList") List<Long> idList);

    List<Long> selectByCategoryIdList(@Param("categoryIdList") List<Long> categoryIdList);

    @InterceptorIgnore(tenantLine = "true")
    List<Question> selectQuestion(@Param("questionIdList") List<Long> questionIdList, @Param("size") int size);

    /**
     * 查找试题所属分类层级最小的分类
     * @param questionId
     * @return
     */
    Category selectMinLevelCategory(Long questionId);

}

