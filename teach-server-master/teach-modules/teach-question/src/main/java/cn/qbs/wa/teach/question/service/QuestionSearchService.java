package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.pojo.question.ESQuestion;
import cn.qbs.wa.teach.question.pojo.question.search.*;

import java.io.IOException;
import java.util.List;

/**
 * 试题搜索接口
 * @Author zcm
 * @Date 2021/11/4 15:56
 * @Version 1.0
 */
public interface QuestionSearchService {

    /**
     * 保存更新索引
     * @param esQuestion
     * @return
     * @throws IOException
     */
    boolean save(ESQuestion esQuestion) throws IOException;

    SearchPageResult<QuestionSearchResult> search(QuestionSearchRequest params);

    /**
     * 删除索引
     * @param id
     */
    void delete(Long id);

    List<CategoryQuestionCount> countCategory(Long orgId) throws IOException;

    QuestionCountResponse count(QuestionCountRequest countRequest) throws IOException;

    /**
     * 随机查询
     * @param params
     * @return
     * @throws IOException
     */
    List<QuestionSearchResult> randomSearch(QuestionRandomSearchParam params) throws IOException;

    void batchDelete(List<Long> questionIdList);

    void deleteByCategoryIdList(List<Long> categoryIdList) throws IOException;

    Long totalQuestion(Long orgId);

}
