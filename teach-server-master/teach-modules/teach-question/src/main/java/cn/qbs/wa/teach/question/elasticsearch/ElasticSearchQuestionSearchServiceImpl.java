package cn.qbs.wa.teach.question.elasticsearch;

import cn.qbs.wa.teach.common.core.domain.SortItem;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.pojo.question.ESQuestion;
import cn.qbs.wa.teach.question.pojo.question.search.*;
import cn.qbs.wa.teach.question.service.QuestionSearchService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcm
 * @Date 2021/11/4 15:58
 * @Version 1.0
 */
@Slf4j
@Service
@RefreshScope
public class ElasticSearchQuestionSearchServiceImpl implements QuestionSearchService {

    @Value("${elasticsearch.indexAlias.question:question_alias}")
    public String indexAlias;

    @Resource
    private RestHighLevelClient esRestClient;


    @Override
    public boolean save(ESQuestion esQuestion) throws IOException {
        if (esQuestion.getUpdateBy() == null) {
            esQuestion.setUpdateBy(esQuestion.getCreateBy());
        }
        if (esQuestion.getUpdateTime() == null) {
            esQuestion.setUpdateTime(esQuestion.getCreateTime());
        }
        IndexRequest indexRequest = new IndexRequest(indexAlias);
        indexRequest.id(String.valueOf(esQuestion.getId()));
        String jsonString = JSON.toJSONString(esQuestion);
        log.info("ES索引试题[{}]--> {}", esQuestion.getId(), jsonString);
        indexRequest.source(jsonString, XContentType.JSON);

        //写入完成立即刷新
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        IndexResponse response = esRestClient.index(indexRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        log.info("新增试题索引结果: {}", result);
        return true;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexAlias, String.valueOf(id));
            //写入完成立即刷新
            deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            DeleteResponse response = esRestClient.delete(deleteRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = response.getResult();
            log.info("删除试题索引结果: {}", result);
        } catch (Exception e) {
            log.error("删除索引失败:", e);
        }
    }

    @Override
    public List<CategoryQuestionCount> countCategory(Long orgId) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexAlias);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("orgId", orgId));

        String aggName = "countCategoryAgg";
        TermsAggregationBuilder countCategoryAgg = AggregationBuilders.terms(aggName);
        countCategoryAgg.field("categoryIds").size(50);
        searchSourceBuilder.aggregation(countCategoryAgg);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);

        log.info("构建的DSL语句: {}", searchSourceBuilder.toString());
        SearchResponse response = esRestClient.search(searchRequest, RequestOptions.DEFAULT);
        ParsedLongTerms agg = response.getAggregations().get(aggName);
        List<CategoryQuestionCount> resultList = new ArrayList<>();
        for (Terms.Bucket bucket : agg.getBuckets()) {
            resultList.add(new CategoryQuestionCount(bucket.getKeyAsNumber().longValue(), bucket.getDocCount()));
        }
        return resultList;
    }

    @Override
    public QuestionCountResponse count(QuestionCountRequest countRequest) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexAlias);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (countRequest.getOrgId() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("orgId", countRequest.getOrgId()));
        }
        if (!CollectionUtils.isEmpty(countRequest.getCategoryIds())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("categoryIds", countRequest.getCategoryIds()));
        }

        boolQueryBuilder.must(QueryBuilders.termQuery("enabled", true));

        searchSourceBuilder.query(boolQueryBuilder);

        String countDifficultyAggName = "countDifficultyAgg";
        TermsAggregationBuilder countDifficultyAgg = AggregationBuilders.terms(countDifficultyAggName);
        countDifficultyAgg.field("difficultyId").size(50);
        searchSourceBuilder.aggregation(countDifficultyAgg);

        String countQuestionTypeAggName = "countQuestionTypeAgg";
        TermsAggregationBuilder countQuestionTypeAgg = AggregationBuilders.terms(countQuestionTypeAggName);
        countQuestionTypeAgg.field("questionTypeId").size(20);
        searchSourceBuilder.aggregation(countQuestionTypeAgg);


        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);

        log.info("构建的DSL语句: {}", searchSourceBuilder.toString());
        SearchResponse response = esRestClient.search(searchRequest, RequestOptions.DEFAULT);

        QuestionCountResponse result = new QuestionCountResponse();
        ParsedLongTerms difficultyAgg = response.getAggregations().get(countDifficultyAggName);
        for (Terms.Bucket bucket : difficultyAgg.getBuckets()) {
            result.getDifficultyList().add(new CountResult(bucket.getKeyAsNumber().longValue(), bucket.getDocCount()));
        }

        ParsedLongTerms questionTypeAgg = response.getAggregations().get(countQuestionTypeAggName);
        for (Terms.Bucket bucket : questionTypeAgg.getBuckets()) {
            result.getQuestionTypeList().add(new CountResult(bucket.getKeyAsNumber().longValue(), bucket.getDocCount()));
        }

        return result;
    }

    @Override
    public List<QuestionSearchResult> randomSearch(QuestionRandomSearchParam param) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexAlias);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("questionTypeId", param.getQuestionTypeQuantity().getQuestionId()));
        if (param.getOrgId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("orgId", param.getOrgId()));
        }
        if (!CollectionUtils.isEmpty(param.getCategoryIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryIds", param.getCategoryIds()));
        }
        if (!CollectionUtils.isEmpty(param.getDifficultyIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("difficultyId", param.getDifficultyIds()));
        }

        // 智能挑题过滤掉禁用的试题
        boolQueryBuilder.filter(QueryBuilders.termQuery("enabled", true));

        Script script = new Script("Math.random()");
        ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.DESC);
        searchSourceBuilder.sort(scriptSortBuilder);

        searchSourceBuilder.from(0).size(param.getQuestionTypeQuantity().getQuantity());
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        log.info("查询参数: {}", param.toString());
        log.info("构建的DSL语句: {}", searchSourceBuilder.toString());
        SearchResponse response = esRestClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        List<QuestionSearchResult> esQuestionList = Arrays.stream(hits).map(i -> {
            String sourceAsString = i.getSourceAsString();
            QuestionSearchResult esQuestion = JSON.parseObject(sourceAsString, QuestionSearchResult.class);
            return esQuestion;
        }).collect(Collectors.toList());
        return esQuestionList;
    }

    @Override
    public void batchDelete(List<Long> questionIdList) {
        if (CollectionUtils.isEmpty(questionIdList)) {
            return;
        }

        try {
            BulkRequest request = new BulkRequest(indexAlias);
            for (Long id : questionIdList) {
                request.add(new DeleteRequest(indexAlias, String.valueOf(id)));
            }

            //写入完成立即刷新
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            boolean hasFailures = esRestClient.bulk(request, RequestOptions.DEFAULT).hasFailures();
            if (hasFailures) {
                throw new ServiceException("删除试题索引失败！");
            }
        } catch (Exception e) {
            log.error("删除试题索引失败:", e);
        }
    }

    @Override
    public void deleteByCategoryIdList(List<Long> categoryIdList) throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexAlias);
        // 设置版本冲突时继续
        request.setConflicts("proceed");
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("categoryIds", categoryIdList);
        request.setQuery(queryBuilder);
        // 刷新索引
        request.setRefresh(true);

        log.info("查询参数: {}", categoryIdList);
        log.info("构建的DSL语句: {}", queryBuilder.toString());

        BulkByScrollResponse response = esRestClient.deleteByQuery(request, RequestOptions.DEFAULT);
//        return response.getStatus().getUpdated();
    }

    @Override
    public Long totalQuestion(Long orgId) {
        QueryBuilder query = QueryBuilders.termQuery("orgId", orgId);
        log.info("ES查询试题总数,构建的DSL语句: {}", query.toString());

        CountRequest countRequest = new CountRequest(new String[]{indexAlias}, query);
        try {
            CountResponse countResponse = esRestClient.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            log.error("ES查询试题总数出现异常：", e);
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public SearchPageResult<QuestionSearchResult> search(QuestionSearchRequest param) {
        //1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            //2、执行检索请求
            SearchResponse response = esRestClient.search(searchRequest, RequestOptions.DEFAULT);

            //3、分析响应数据，封装成我们需要的格式
            SearchPageResult<QuestionSearchResult> searchResult = buildSearchResult(response, param);
            return searchResult;
        } catch (IOException e) {
            log.error("题库搜索出现异常：", e);
            e.printStackTrace();
        }

        return new SearchPageResult<>();
    }

    private SearchPageResult<QuestionSearchResult> buildSearchResult(SearchResponse response, QuestionSearchRequest param) {
        SearchPageResult pageResult = new SearchPageResult();
        List<QuestionSearchResult> questionList = new ArrayList<>();
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        if (searchHits.length > 0) {
            for (SearchHit hit : searchHits) {
                String sourceAsString = hit.getSourceAsString();
                QuestionSearchResult esQuestion = JSON.parseObject(sourceAsString, QuestionSearchResult.class);
                if (StringUtils.hasText(param.getKeyword())) {
                    HighlightField highlightField = hit.getHighlightFields().get("content");
                    String highlightContent = highlightField.getFragments()[0].toString();
                    esQuestion.setContent(highlightContent);
                }
                questionList.add(esQuestion);
            }
        }

        pageResult.setList(questionList);
        pageResult.setPageNum(param.getPageNum());
        pageResult.setPageSize(param.getPageSize());
        pageResult.setTotal(hits.getTotalHits().value);
        return pageResult;
    }

    private SearchRequest buildSearchRequest(QuestionSearchRequest param) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (StringUtils.hasText(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("content", param.getKeyword()));
        }
        if (param.getOrgId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("orgId", param.getOrgId()));
        }
        if (param.getEnabled() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("enabled", param.getEnabled()));
        }
        if (!CollectionUtils.isEmpty(param.getCategoryIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("categoryIds", param.getCategoryIds()));
        }
        if (!CollectionUtils.isEmpty(param.getQuestionTypeIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("questionTypeId", param.getQuestionTypeIds()));
        }
        if (!CollectionUtils.isEmpty(param.getDifficultyIds())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("difficultyId", param.getDifficultyIds()));
        }
        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.sort("_score", SortOrder.DESC);
        searchSourceBuilder.sort("weight", SortOrder.DESC);
        if (CollectionUtils.isNotEmpty(param.getSortItemList())) {
            for (SortItem sortItem : param.getSortItemList()) {
                SortOrder sortOrder = "desc".equalsIgnoreCase(sortItem.getOrder()) ? SortOrder.DESC : SortOrder.ASC;
                searchSourceBuilder.sort(sortItem.getAttr(), sortOrder);
            }
        }

        searchSourceBuilder.from((param.getPageNum() - 1) * param.getPageSize());
        searchSourceBuilder.size(param.getPageSize());

        if (StringUtils.hasText(param.getKeyword())) {
            HighlightBuilder highlightBuilder = SearchSourceBuilder.highlight();
            highlightBuilder.field("content");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            highlightBuilder.numOfFragments(0);
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        log.info("查询参数: {}", param.toString());
        log.info("构建的DSL语句: {}", searchSourceBuilder.toString());
        SearchRequest searchRequest = new SearchRequest(new String[]{indexAlias}, searchSourceBuilder);
        return searchRequest;
    }

}
