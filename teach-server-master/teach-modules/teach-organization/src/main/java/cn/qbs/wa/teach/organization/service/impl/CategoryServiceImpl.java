package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.organization.entity.Category;
import cn.qbs.wa.teach.organization.mapper.CategoryMapper;
import cn.qbs.wa.teach.organization.pojo.category.*;
import cn.qbs.wa.teach.organization.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分类(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-01-18 09:48:41
 */
@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public boolean add(CategoryAddRequest params) {
        Category category = new Category();
        BeanUtils.copyProperties(params, category);
        return this.save(category);
    }

    @Override
    public IPage<CategoryPageResponse> page(CategoryPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CategoryDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CategoryUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Category category = new Category();
        BeanUtils.copyProperties(params, category);
        return this.updateById(category);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            List<Category> list = list(new LambdaQueryWrapper<Category>().in(Category::getParentId, idList));
            if (CollUtil.isNotEmpty(list)) {
                throw new ServiceException("存在子级分类,无法删除!");
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public List<CategoryTreeResponse> tree(CategoryTreeRequest params) {
        List<Category> categoryList = list(new LambdaQueryWrapper<Category>().eq(Category::getEnabled, Constants.DB_TRUE).orderByAsc(Category::getSort));
        if (CollectionUtils.isNotEmpty(categoryList)) {
            List<CategoryTreeResponse> categoryTreeResponses = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryTreeResponse categoryTreeResponse = new CategoryTreeResponse();
                BeanUtils.copyProperties(category, categoryTreeResponse);
                categoryTreeResponses.add(categoryTreeResponse);
            }

            return (List<CategoryTreeResponse>) TreeUtil.tree(categoryTreeResponses);
        }
        return null;
    }

    @Override
    public List<CategoryListResponse> childList(cn.qbs.wa.teach.organization.pojo.category.CategoryListRequest params) {
        List<Category> categoryList = list(new LambdaQueryWrapper<Category>().eq(Category::getParentId, params.getParentId()).orderByAsc(Category::getSort));
        return BeanUtil.copyToList(categoryList, CategoryListResponse.class);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Category> categoryList = new ArrayList<>();
        for (Long id : idList) {
            Category category = new Category();
            category.setId(id);
            category.setEnabled(flag);
            categoryList.add(category);
        }
        updateBatchById(categoryList);
    }

}

