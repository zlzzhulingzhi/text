package cn.qbs.wa.teach.question.elasticsearch;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.pojo.paper.EsPaper;
import cn.qbs.wa.teach.question.pojo.paper.PaperSearchRequest;
import cn.qbs.wa.teach.question.service.ElasticSearchPaperService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zcm
 * @Date 2021/11/4 15:58
 * @Version 1.0
 */
@Slf4j
@Service
@RefreshScope
public class ElasticSearchPaperServiceImpl implements ElasticSearchPaperService {

    @Value("${elasticsearch.indexAlias.paper:paper_alias}")
    public String indexAlias;

    @Resource
    private RestHighLevelClient esRestClient;


    @Override
    public boolean save(EsPaper esPaper) throws IOException {
        if (esPaper.getUpdateBy() == null) {
            esPaper.setUpdateBy(esPaper.getCreateBy());
        }
        if (esPaper.getUpdateTime() == null) {
            esPaper.setUpdateTime(esPaper.getCreateTime());
        }
        IndexRequest indexRequest = new IndexRequest(indexAlias);
        indexRequest.id(String.valueOf(esPaper.getId()));
        String jsonString = JSON.toJSONString(esPaper);
        log.info("ES索引试卷[{}]--> {}", esPaper.getId(), jsonString);
        indexRequest.source(jsonString, XContentType.JSON);
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        IndexResponse response = esRestClient.index(indexRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        log.info("新增试卷索引结果: {}", result);
        return true;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexAlias, String.valueOf(id));
            deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            DeleteResponse response = esRestClient.delete(deleteRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = response.getResult();
            log.info("删除试卷索引结果: {}", result);
        } catch (Exception e) {
            log.error("删除索引失败:", e);
        }
    }

    @Override
    public void batchDelete(List<Long> paperIdList) {
        if (CollectionUtils.isEmpty(paperIdList)) {
            return;
        }

        try {
            BulkRequest request = new BulkRequest(indexAlias);
            for (Long id : paperIdList) {
                request.add(new DeleteRequest(indexAlias, String.valueOf(id)));
            }
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            boolean hasFailures = esRestClient.bulk(request, RequestOptions.DEFAULT).hasFailures();
            if (hasFailures) {
                throw new ServiceException("删除试卷索引失败！");
            }
        } catch (Exception e) {
            log.error("删除试卷索引失败:", e);
            throw new ServiceException("删除试卷索引失败！");
        }
    }

    @Override
    public SearchPageResult<EsPaper> search(PaperSearchRequest param) {
        if (StringUtils.isBlank(param.getSortField())) {
            param.setSortField("createTime");
            param.setSortOrder("desc");
        }
        //1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            //2、执行检索请求
            SearchResponse response = esRestClient.search(searchRequest, RequestOptions.DEFAULT);

            //3、分析响应数据，封装成我们需要的格式
            SearchPageResult<EsPaper> searchResult = buildSearchResult(response, param);
            return searchResult;
        } catch (IOException e) {
            log.error("题库搜索出现异常：", e);
            e.printStackTrace();
        }

        return new SearchPageResult<>();
    }

    private SearchRequest buildSearchRequest(PaperSearchRequest param) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (param.getOrgId() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("orgId", param.getOrgId()));
        }
        if (CollectionUtils.isNotEmpty(param.getCategoryIds())) {
            TermsQueryBuilder categoryIdTermsQueryBuilder = QueryBuilders.termsQuery("categoryList.id", param.getCategoryIds());
            boolQueryBuilder.must(QueryBuilders.nestedQuery("categoryList", categoryIdTermsQueryBuilder, ScoreMode.None));
        }
        if (StringUtils.isNotBlank(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", param.getKeyword()));
        }
        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.sort("_score", SortOrder.DESC);
        if (StringUtils.isNotBlank(param.getSortField())) {
            SortOrder sortOrder = "desc".equalsIgnoreCase(param.getSortOrder()) ? SortOrder.DESC : SortOrder.ASC;
            searchSourceBuilder.sort(param.getSortField(), sortOrder);
        }

        searchSourceBuilder.from((param.getPageNum() - 1) * param.getPageSize());
        searchSourceBuilder.size(param.getPageSize());

        if (StringUtils.isNotBlank(param.getKeyword())) {
            HighlightBuilder highlightBuilder = SearchSourceBuilder.highlight();
            highlightBuilder.field("name");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        log.info("查询参数: {}", param.toString());
        log.info("构建的DSL语句: {}", searchSourceBuilder.toString());
        SearchRequest searchRequest = new SearchRequest(new String[]{indexAlias}, searchSourceBuilder);
        return searchRequest;
    }

    private SearchPageResult<EsPaper> buildSearchResult(SearchResponse response, PaperSearchRequest param) {
        SearchPageResult pageResult = new SearchPageResult();
        List<EsPaper> paperList = new ArrayList<>();
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        if (searchHits.length > 0) {
            for (SearchHit hit : searchHits) {
                String sourceAsString = hit.getSourceAsString();
                EsPaper esPaper = JSON.parseObject(sourceAsString, EsPaper.class);
                esPaper.setOriginalName(esPaper.getName());
                if (StringUtils.isNotBlank(param.getKeyword())) {
                    HighlightField highlightField = hit.getHighlightFields().get("name");
                    String highlightContent = highlightField.getFragments()[0].toString();
                    esPaper.setName(highlightContent);
                }
                paperList.add(esPaper);
            }
        }

        pageResult.setList(paperList);
        pageResult.setPageNum(param.getPageNum());
        pageResult.setPageSize(param.getPageSize());
        pageResult.setTotal(hits.getTotalHits().value);
        return pageResult;
    }

}
