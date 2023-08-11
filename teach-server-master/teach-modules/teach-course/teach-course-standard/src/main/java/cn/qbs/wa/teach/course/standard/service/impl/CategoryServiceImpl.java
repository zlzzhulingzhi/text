package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.course.common.entity.Category;
import cn.qbs.wa.teach.course.common.entity.CategoryDistribution;
import cn.qbs.wa.teach.course.standard.mapper.CategoryDistributionMapper;
import cn.qbs.wa.teach.course.standard.mapper.CategoryMapper;
import cn.qbs.wa.teach.course.standard.pojo.category.*;
import cn.qbs.wa.teach.course.standard.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【课程分类】(Category)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Slf4j
@RefreshScope
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 分类最大层级
     */
    @Value("${system.categoryMaxLevel:3}")
    private int categoryMaxLevel;

    @Resource
    private CategoryDistributionServiceImpl categoryDistributionService;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryDistributionMapper categoryDistributionMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Category add(CategoryAddRequest params) {
        String name = params.getCategoryName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Category::getCategoryName, name)
                .eq(Category::getParentId, parentId)
                .count();
        if (count > 0) {
            throw new ServiceException("已存在同名分类！");
        }

        Category category = new Category();
        BeanUtils.copyProperties(params, category);

        if (parentId == 0) {
            category.setParentCode("0");
            category.setFullName(params.getCategoryName());
        } else {
            Category parent = this.getById(parentId);
            if (parent == null) {
                throw new ServiceException("父节点不存在！");
            }

            category.setParentCode(parent.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId);
            if (category.getParentCode().split(Constants.CATEGORY_PARENT_CODE_SEPARATOR).length > categoryMaxLevel) {
                throw new ServiceException("层级不能超过" + categoryMaxLevel + "级！");
            }

            category.setFullName(parent.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + params.getCategoryName());
        }

        if (params.getSort() == null) {
            category.setSort(selectMaxSortNum(parentId) + 1);
        }

        if (StrUtil.isBlank(params.getCategoryCode())) {
            category.setCategoryCode(IdWorker.get32UUID().toUpperCase());
        }

        boolean save = this.save(category);
        if (save) {
            // 创建课程分类所包含课程数记录
            CategoryDistribution distribution = new CategoryDistribution();
            distribution.setCategoryId(category.getId());
            distribution.setCategoryCode(category.getCategoryCode());
            distribution.setCount(0);
            categoryDistributionService.save(distribution);
        }
        return category;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Category init(CategoryAddRequest params) {

        Category category = new Category();
        BeanUtils.copyProperties(params, category);
        category.setParentId(params.getParentId());
        category.setOrgId(params.getOrgId());
        category.setParentCode("0");
        category.setFullName(params.getCategoryName());
        category.setSort(params.getSort());
        if (StrUtil.isBlank(params.getCategoryCode())) {
            category.setCategoryCode(IdWorker.get32UUID().toUpperCase());
        }
        List<Category> list=new ArrayList<>();
        list.add(category);
        categoryMapper.insertBatch(list);
        CategoryDistribution distribution = new CategoryDistribution();
        distribution.setCategoryId(category.getId());
        distribution.setCategoryCode(category.getCategoryCode());
        distribution.setCount(0);
        distribution.setOrgId(category.getOrgId());
        List<CategoryDistribution> categoryDistributions=new ArrayList<>();
        categoryDistributions.add(distribution);
        categoryDistributionMapper.insertBatch(categoryDistributions);
        return category;
    }

    /**
     * 查询当前分类下的分类的最大排序号
     * @param parentId
     * @return
     */
    private int selectMaxSortNum(Long parentId) {
        Category category = this.getBaseMapper().selectOne(new QueryWrapper<Category>().eq("parent_id", parentId).select("IFNULL(max(sort),0) as sort"));
        return category.getSort();
    }

    @Override
    public IPage<CategoryPageResponse> page(CategoryPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public List<CategoryListResponse> list(CategoryListRequest params) {
        return baseMapper.list(params);
    }

    @Override
    public CategoryDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CategoryUpdateRequest params) {
        Long categoryId = params.getId();
        Category category = this.getById(categoryId);
        if (category == null) {
            throw new IllegalParamsException("分类不存在！");
        }

        String beforeName = category.getCategoryName();
        Long beforeParentId = category.getParentId();
        String name = params.getCategoryName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Category::getCategoryName, name)
                .eq(Category::getParentId, parentId)
                .ne(Category::getId, categoryId)
                .count();
        if (count > 0) {
            throw new ServiceException("已存在同名分类！");
        }

        if (parentId > 0) {
            Category parent = this.getById(parentId);
            if (parent == null) {
                throw new ServiceException("父节点不存在！");
            }

            category.setParentCode(parent.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId);
            if (category.getParentCode().split(Constants.CATEGORY_PARENT_CODE_SEPARATOR).length > categoryMaxLevel) {
                throw new ServiceException("层级不能超过" + categoryMaxLevel + "级！");
            }

            category.setFullName(parent.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + params.getCategoryName());
        } else {
            category.setParentCode("0");
            category.setFullName(params.getCategoryName());
        }

        category.setCategoryName(params.getCategoryName());
        category.setParentId(parentId);
        category.setEnabled(params.getEnabled());
        category.setSort(params.getSort());
        category.setCategoryIconUrl(params.getCategoryIconUrl());
        category.setUpdateBy(SecurityContextHolder.getUserId());
        category.setUpdateTime(LocalDateTime.now());

        if (category.getSort() == null) {
            category.setSort(selectMaxSortNum(params.getParentId()) + 1);
        }

        boolean success = this.updateById(category);
        // 修改了父节点或者分类名称，则同步更新子类
        if (!parentId.equals(beforeParentId) || !category.getCategoryName().equals(beforeName)) {
            this.updateChildren(category);
        }

        return success;
    }

    private void updateChildren(Category category) {
        if (category != null) {
            Long parentId = category.getId();
            List<Category> children = this.lambdaQuery().eq(Category::getParentId, parentId).list();
            if (CollectionUtils.isNotEmpty(children)) {
                for (Category child : children) {
                    String parentCode = category.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId;
                    String fullName = category.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + child.getCategoryName();
                    this.lambdaUpdate().eq(Category::getId, child.getId())
                            .set(Category::getParentCode, parentCode)
                            .set(Category::getFullName, fullName)
                            .set(Category::getUpdateBy, category.getUpdateBy())
                            .set(Category::getUpdateTime, new Date())
                            .update();

                    child.setParentCode(parentCode);
                    child.setFullName(fullName);
                    updateChildren(child);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Long> idList) {
        // 删除关联的课程分类分布统计
        categoryDistributionService.remove(Wrappers.<CategoryDistribution>lambdaQuery().in(CategoryDistribution::getCategoryId, idList));
        return this.removeByIds(idList);
    }

    @Override
    public List<CategoryTreeResponse> tree(CategoryTreeRequest params) {
        if (params.getEnabled() == null) {
            params.setEnabled(Constants.DB_TRUE);
        }
        return treeV2(params);
    }

    @Override
    public boolean hasChildren(Long id) {
        return this.count(Wrappers.<Category>lambdaQuery().eq(Category::getParentId,id)) > 0;
    }

    @Override
    public List<CategoryTreeResponse> treeV2(CategoryTreeRequest params) {
        List<Category> list = this.lambdaQuery()
                .eq(params.getEnabled() != null, Category::getEnabled, params.getEnabled())
                .eq(params.getParentId() != null, Category::getParentId, params.getParentId())
                .list();
        if (!list.isEmpty()) {
            List<CategoryTreeResponse> responseList = list.stream().map(e -> {
                CategoryTreeResponse treeResponse = new CategoryTreeResponse();
                BeanUtils.copyProperties(e, treeResponse);
                return treeResponse;
            }).collect(Collectors.toList());
            return TreeUtil.tree(responseList);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean enable(List<Long> categoryIdList, Integer enabled) {
        boolean success = this.lambdaUpdate().in(Category::getId, categoryIdList)
                .set(Category::getEnabled, enabled)
                .set(Category::getUpdateBy, SecurityContextHolder.getUserId())
                .set(Category::getUpdateTime, LocalDateTime.now())
                .update();
        return success;
    }

}

