package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.organization.entity.News;
import cn.qbs.wa.teach.organization.pojo.news.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻(News)表服务接口
 *
 * @author makejava
 * @since 2022-01-18 11:30:46
 */
public interface NewsService extends IService<News> {

    /**
     * 新增新闻
     * @param params
     * @return
     */
    boolean add(NewsAddRequest params);

    /**
     * 分页查询新闻
     * @param params
     * @return
     */
    IPage<NewsPageResponse> page(NewsPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    NewsDetailResponse detail(Serializable id);

    /**
     * 更新新闻
     * @param params
     * @return
     */
    boolean update(NewsUpdateRequest params);

    /**
     * 删除新闻
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);


    void batchStatus(Integer flag, List<Long> idList);

    void updateViews(IdRequest params);
}

