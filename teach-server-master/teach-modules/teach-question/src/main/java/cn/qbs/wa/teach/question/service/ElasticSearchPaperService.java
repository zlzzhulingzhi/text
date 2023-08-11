package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.question.elasticsearch.SearchPageResult;
import cn.qbs.wa.teach.question.pojo.paper.EsPaper;
import cn.qbs.wa.teach.question.pojo.paper.PaperSearchRequest;

import java.io.IOException;
import java.util.List;

/**
 * 试卷搜索接口
 * @Author zcm
 * @Date 2021/11/22 15:56
 * @Version 1.0
 */
public interface ElasticSearchPaperService {

    /**
     * 保存更新索引
     * @param esPaper
     * @return
     * @throws IOException
     */
    boolean save(EsPaper esPaper) throws IOException;

    SearchPageResult<EsPaper> search(PaperSearchRequest params);

    /**
     * 删除索引
     * @param id
     */
    void delete(Long id);

    void batchDelete(List<Long> paperIdList);

}
