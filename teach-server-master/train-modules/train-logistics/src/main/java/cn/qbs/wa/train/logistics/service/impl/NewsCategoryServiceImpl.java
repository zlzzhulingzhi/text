package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.NewsCategory;
import cn.qbs.wa.train.logistics.mapper.NewsCategoryMapper;
import cn.qbs.wa.train.logistics.pojo.newscategory.*;
import cn.qbs.wa.train.logistics.service.NewsCategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻-分类关系表(NewsCategory)表服务实现类
 *
 * @author makejava
 * @since 2022-01-18 09:30:05
 */
@Slf4j
@Service("newsCategoryService")
public class NewsCategoryServiceImpl extends ServiceImpl<NewsCategoryMapper, NewsCategory> implements NewsCategoryService {

    @Override
    public boolean add(NewsCategoryAddRequest params) {
        NewsCategory newsCategory = new NewsCategory();
        BeanUtils.copyProperties(params, newsCategory);
        return this.save(newsCategory);
    }

    @Override
    public IPage<NewsCategoryPageResponse> page(NewsCategoryPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public NewsCategoryDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(NewsCategoryUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        NewsCategory newsCategory = new NewsCategory();
        BeanUtils.copyProperties(params, newsCategory);
        return this.updateById(newsCategory);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

