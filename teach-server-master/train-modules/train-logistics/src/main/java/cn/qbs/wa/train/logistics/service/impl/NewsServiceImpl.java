package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.logistics.entity.News;
import cn.qbs.wa.train.logistics.entity.NewsCategory;
import cn.qbs.wa.train.logistics.mapper.NewsMapper;
import cn.qbs.wa.train.logistics.pojo.news.*;
import cn.qbs.wa.train.logistics.service.NewsCategoryService;
import cn.qbs.wa.train.logistics.service.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻(News)表服务实现类
 *
 * @author makejava
 * @since 2022-01-18 11:30:46
 */
@Slf4j
@Service("newsService")
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    NewsCategoryService newsCategoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(NewsAddRequest params) {
        News news = new News();
        BeanUtils.copyProperties(params, news);
        boolean saveFlag = this.save(news);
        saveNewsCategoryBatch(params.getCategoryIdList(), news.getId());
        return saveFlag;
    }

    private void saveNewsCategoryBatch(List<Long> categoryIdList, Long newsId) {
        if (CollUtil.isNotEmpty(categoryIdList)) {
            List<NewsCategory> categoryList = new ArrayList<>();
            for (Long categoryId : categoryIdList) {
                NewsCategory newsCategory = new NewsCategory();
                newsCategory.setCategoryId(categoryId);
                newsCategory.setNewsId(newsId);
                categoryList.add(newsCategory);
            }
            newsCategoryService.saveBatch(categoryList);
        }
    }

    @Override
    public IPage<NewsPageResponse> page(NewsPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public NewsDetailResponse detail(Serializable id) {
        NewsDetailResponse newsDetailResponse = baseMapper.selectDetailById(id);
        List<NewsCategory> newsCategoryList = newsCategoryService.list(new LambdaQueryWrapper<NewsCategory>().eq(NewsCategory::getNewsId, id));
        if (CollUtil.isNotEmpty(newsCategoryList)) {
            newsDetailResponse.setCategoryIdList(newsCategoryList.stream().map(NewsCategory::getCategoryId).collect(Collectors.toList()));
        }
        return newsDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(NewsUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        News news = new News();
        BeanUtils.copyProperties(params, news);
        boolean updateFlag = this.updateById(news);
        newsCategoryService.remove(new LambdaQueryWrapper<NewsCategory>().eq(NewsCategory::getNewsId, params.getId()));
        saveNewsCategoryBatch(params.getCategoryIdList(), params.getId());
        return updateFlag;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }


    @Override
    public void batchStatus(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<News> newsList = new ArrayList<>();
        for (Long newId : idList) {
            News news = new News();
            news.setId(newId);
            news.setShelfStatus(flag);
            newsList.add(news);
        }
        updateBatchById(newsList);
    }


    @Override
    public void updateViews(IdRequest params) {
        News news = getById(params.getId());
        if (news != null) {
            News newsViews = new News();
            newsViews.setId(params.getId());
            newsViews.setViews(news.getViews() + 1);
            updateById(newsViews);
        }
    }

}

