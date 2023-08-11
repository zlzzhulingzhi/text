package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.NewsCategory;
import cn.qbs.wa.train.logistics.pojo.newscategory.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻-分类关系表(NewsCategory)表服务接口
 *
 * @author makejava
 * @since 2022-01-18 09:30:04
 */
public interface NewsCategoryService extends IService<NewsCategory> {

    /**
     * 新增新闻-分类关系表
     * @param params
     * @return
     */
    boolean add(NewsCategoryAddRequest params);

    /**
     * 分页查询新闻-分类关系表
     * @param params
     * @return
     */
    IPage<NewsCategoryPageResponse> page(NewsCategoryPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    NewsCategoryDetailResponse detail(Serializable id);

    /**
     * 更新新闻-分类关系表
     * @param params
     * @return
     */
    boolean update(NewsCategoryUpdateRequest params);

    /**
     * 删除新闻-分类关系表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

