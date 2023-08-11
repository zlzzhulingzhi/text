package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.exam.admin.mapper.CategoryMapper;
import cn.qbs.wa.teach.exam.admin.pojo.category.*;
import cn.qbs.wa.teach.exam.admin.service.CategoryService;
import cn.qbs.wa.teach.exam.admin.service.ExamCategoryService;
import cn.qbs.wa.teach.exam.common.entity.Category;
import cn.qbs.wa.teach.exam.common.entity.ExamCategory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分类(Category)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:34:12
 */
@Slf4j
@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ExamCategoryService examCategoryService;


    @Override
    public Long add(CategoryAddRequest params) {
        String name = params.getName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Category::getName, name)
                .eq(Category::getParentId, parentId)
                .count();
        if (count > 0) {
            throw new ServiceException("已存在同名分类！");
        }

        Category category = new Category();
        BeanUtils.copyProperties(params, category);

        if (parentId != 0) {
            Category parent = this.getById(parentId);
            if (parent == null) {
                throw new ServiceException("父节点不存在！");
            }
        }

        if (category.getSort() == null) {
            Integer maxSortNum = this.baseMapper.selectMaxSortNum(parentId);
            category.setSort(++maxSortNum);
        }

        this.save(category);
        return category.getId();
    }

    @Override
    public IPage<CategoryPageResponse> page(CategoryPageRequest params) {
        if (params.getParentId() == null) {
            params.setParentId(0L);
        }
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CategoryDetailResponse detail(Serializable id) {
        CategoryDetailResponse detail = baseMapper.selectDetailById(id);
        if (detail != null) {
            Long parentId = detail.getParentId();
            if (parentId != null && parentId > 0) {
                Category parent = this.getById(parentId);
                detail.setParent(parent);
            }

            List<Category> children = this.lambdaQuery().eq(Category::getParentId, detail.getId()).eq(Category::getEnabled, true).list();
            detail.setChildren(children);
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(CategoryUpdateRequest params) {
        Long categoryId = params.getId();
        if (categoryId == null) {
            throw new IllegalParamsException("ID不能为空！");
        }

        Category category = this.getById(categoryId);
        if (category == null) {
            throw new IllegalParamsException("分类不存在！");
        }

        String beforeName = category.getName();
        Long beforeParentId = category.getParentId();
        String name = params.getName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Category::getName, name)
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
        }

        category.setName(params.getName());
        category.setParentId(parentId);
        category.setEnabled(params.getEnabled());
        category.setSort(params.getSort());
        if (category.getSort() == null) {
            Integer maxSortNum = this.baseMapper.selectMaxSortNum(parentId);
            category.setSort(++maxSortNum);
        }

        boolean success = this.updateById(category);
        if (parentId != null && !parentId.equals(beforeParentId)) {
            this.updateChildren(category);
        }

        // TODO 暂未将分类名称存入ES，所以不需要更新ES
        /*if (!name.equals(beforeName)) {
            mqProducerService.sendCategoryNameChange(categoryId, SecurityContextHolder.getOrgId());
        }*/
        return success;
    }

    @Override
    public void updateChildren(Category category) {
        if (category != null) {
            Long parentId = category.getId();
            List<Category> children = this.lambdaQuery().eq(Category::getParentId, parentId).list();
            if (CollectionUtils.isNotEmpty(children)) {
                for (Category basicGradePO : children) {
                    this.lambdaUpdate().eq(Category::getId, basicGradePO.getId())
                            .set(Category::getUpdateBy, category.getUpdateBy())
                            .set(Category::getUpdateTime, new Date())
                            .update();
                }
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        Long count = this.lambdaQuery().eq(Category::getParentId, id).count();
        if (count != null && count > 0) {
            throw new ServiceException("当前分类下存在子分类，不能删除！");
        }

        Category category = this.getById(id);
        if (category == null) {
            throw new ServiceException("分类不存在！");
        }

        count = this.examCategoryService.lambdaQuery().eq(ExamCategory::getCategoryId, id).count();
        if (count != null && count > 0) {
            throw new ServiceException("当前分类下存在试题，不能删除！");
        }

        return this.removeById(id);
    }

    @Override
    public boolean enable(EnableRequest request) {
        return this.lambdaUpdate().in(Category::getId, request.getIdList()).set(Category::getEnabled, request.getEnabled()).update();
    }

    @Override
    public List<?> getTreeList() {
        List<Category> categoryList = this.lambdaQuery().eq(Category::getEnabled, true).list();
        List<CategoryTreeNode> treeNodeList = new ArrayList<>(categoryList.size());
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (Category category : categoryList) {
                CategoryTreeNode treeNode = new CategoryTreeNode();
                BeanUtils.copyProperties(category, treeNode);
                treeNodeList.add(treeNode);
            }
            return TreeUtil.tree(treeNodeList);
        }

        return treeNodeList;
    }

    @Override
    public List<CategoryPageResponse> childrenList(Long parentId) {
        if (parentId != null) {
            return baseMapper.childrenList(parentId);
        }
        return null;
    }

}



