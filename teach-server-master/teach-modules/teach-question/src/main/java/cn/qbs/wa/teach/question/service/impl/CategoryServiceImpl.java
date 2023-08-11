package cn.qbs.wa.teach.question.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.question.entity.Category;
import cn.qbs.wa.teach.question.entity.PaperCategory;
import cn.qbs.wa.teach.question.entity.QuestionCategory;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.mapper.CategoryMapper;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.category.*;
import cn.qbs.wa.teach.question.service.CategoryService;
import cn.qbs.wa.teach.question.service.MqProducerService;
import cn.qbs.wa.teach.question.service.PaperCategoryService;
import cn.qbs.wa.teach.question.service.QuestionCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试题分类(QuestionCategory)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:43:33
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
    private QuestionCategoryService questionCategoryService;

    @Resource
    private PaperCategoryService paperCategoryService;

    @Resource
    private MqProducerService mqProducerService;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Category init(QuestionCategoryAddRequest params) {
        Category category = new Category();
        BeanUtils.copyProperties(params, category);
        category.setParentId(params.getParentId());
        category.setOrgId(params.getOrgId());
        category.setParentCode("0");
        category.setFullName(params.getName());
        category.setSortNum(params.getSortNum());
        List<Category> list=new ArrayList<>();
        list.add(category);
        categoryMapper.insertBatch(list);
        return category;
    }


    @Override
    public Long add(CategoryAddRequest params) {
        validCategoryGroupId(params.getGroupId());

        String name = params.getName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Category::getName, name)
                .eq(Category::getGroupId, params.getGroupId())
                .eq(Category::getParentId, parentId)
                .count();
        if (count > 0) {
            throw new ServiceException("已存在同名分类！");
        }

        Category category = new Category();
        BeanUtils.copyProperties(params, category);

        if (parentId == 0) {
            category.setLevel(1);
            category.setParentCode("0");
            category.setFullName(params.getName());
        } else {
            Category parent = this.getById(parentId);
            if (parent == null) {
                throw new ServiceException("父节点不存在！");
            }
            int level = parent.getLevel() + 1;
            if (level > categoryMaxLevel) {
                throw new ServiceException("层级不能超过" + categoryMaxLevel + "级！");
            }
            category.setLevel(level);
            category.setParentCode(parent.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId);
            category.setFullName(parent.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + params.getName());
        }

        if (category.getSortNum() == null) {
            category.setSortNum(selectMaxSortNum(parentId) + 1);
        }

        this.save(category);
        return category.getId();
    }

    private void validCategoryGroupId(Integer groupId) {
        if (groupId == null) {
            throw new IllegalParamsException("分类分组ID不能为空！");
        }
        CategoryGroupEnum categoryGroupEnum = CategoryGroupEnum.fromId(groupId);
        if (categoryGroupEnum == null) {
            throw new ServiceException("不存在的分组！");
        }
    }

    /**
     * 查询当前分类下的分类的最大排序号
     * @param parentId
     * @return
     */
    private int selectMaxSortNum(Long parentId) {
        Category category = this.getBaseMapper().selectOne(new QueryWrapper<Category>().eq("parent_id", parentId).select("IFNULL(max(sort_num),0) as sortNum"));
        return category.getSortNum();
    }

    @Override
    public IPage<CategoryPageResponse> page(CategoryPageRequest params) {
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
                .eq(Category::getGroupId, category.getGroupId())
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
            int level = parent.getLevel() + 1;
            if (level > categoryMaxLevel) {
                throw new ServiceException("层级不能超过" + categoryMaxLevel + "级！");
            }
            category.setLevel(level);
            category.setParentCode(parent.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId);
            category.setFullName(parent.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + params.getName());
        } else {
            category.setLevel(1);
            category.setParentCode("0");
            category.setFullName(params.getName());
        }

        category.setName(params.getName());
        category.setParentId(parentId);
        category.setEnabled(params.getEnabled());
        category.setUpdateBy(SecurityContextHolder.getUserId());
        category.setUpdateTime(LocalDateTime.now());
        category.setSortNum(params.getSortNum());
        if (category.getSortNum() == null) {
            category.setSortNum(selectMaxSortNum(parentId) + 1);
        }

        boolean success = this.updateById(category);
        // 修改了父节点或者分类名称，则同步更新子类
        if (!parentId.equals(beforeParentId) || !category.getName().equals(beforeName)) {
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
                for (Category child : children) {
                    int level = category.getLevel() + 1;
                    String parentCode = category.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId;
                    String fullName = category.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + child.getName();
                    this.lambdaUpdate().eq(Category::getId, child.getId())
                            .set(Category::getParentCode, parentCode)
                            .set(Category::getFullName, fullName)
                            .set(Category::getLevel, level)
                            .set(Category::getUpdateBy, category.getUpdateBy())
                            .set(Category::getUpdateTime, new Date())
                            .update();

                    child.setParentCode(parentCode);
                    child.setFullName(fullName);
                    child.setLevel(level);
                    updateChildren(child);
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

        count = this.questionCategoryService.lambdaQuery().eq(QuestionCategory::getCategoryId, id).count();
        if (count != null && count > 0) {
            throw new ServiceException("当前分类下存在试题，不能删除！");
        }

        count = this.paperCategoryService.lambdaQuery().eq(PaperCategory::getCategoryId, id).count();
        if (count != null && count > 0) {
            throw new ServiceException("当前分类下存在试卷，不能删除！");
        }

        return this.removeById(id);
    }

    @Override
    public boolean enable(List<Long> categoryIdList, Integer enabled) {
        boolean success = this.lambdaUpdate().in(Category::getId, categoryIdList)
                .set(Category::getEnabled, enabled)
                .set(Category::getUpdateBy, SecurityContextHolder.getUserId())
                .set(Category::getUpdateTime, LocalDateTime.now())
                .update();

        // 禁用分类下的试题，仍然可以查询出来
        /*Category category = this.getById(categoryIdList.get(0));
        Integer groupId = category.getGroupId();
        if (enabled.equals(0)) {
            mqProducerService.sendDisableCategoryMsg(categoryIdList, SecurityContextHolder.getOrgId(), groupId);
        } else if (enabled.equals(1)) {
            mqProducerService.sendEnableCategoryMsg(categoryIdList, SecurityContextHolder.getOrgId(), groupId);
        }*/
        return success;
    }

    @Override
    public List<SelectOption> getSelectOptionList() {
        List<Category> categoryList = this.lambdaQuery()
                .eq(Category::getEnabled, true)
                .orderByAsc(Category::getSortNum)
                .list();
        return categoryList.stream().map(c ->
                new SelectOption(c.getId(), c.getName(), c.getSortNum())
        ).collect(Collectors.toList());
    }

    @Override
    public List<CategoryTreeNode> getTreeList(Integer groupId, Boolean enabled) {
        List<Category> categoryList = getByGroupId(groupId, enabled);
        List<CategoryTreeNode> treeNodeList = new ArrayList<>(categoryList.size());
        if (CollectionUtils.isNotEmpty(categoryList)) {
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
    public List<Category> getByGroupId(Integer groupId, Boolean enabled) {
        validCategoryGroupId(groupId);
        return this.lambdaQuery().eq(Category::getGroupId, groupId)
                .eq(enabled != null, Category::getEnabled, enabled)
                .orderByAsc(Category::getLevel)
                .orderByAsc(Category::getSortNum)
                .orderByAsc(Category::getId)
                .list();
    }

    /**
     * 更新缓存
     * @param category
     */
    @Override
    public void updateCache(Category category) {
        //
    }

    @Override
    public List<CategoryPageResponse> childrenList(Long parentId) {
        if (parentId != null) {
            return baseMapper.childrenList(parentId);
        }
        return null;
    }

    @Override
    public Category selectFirst(Long categoryId, Long orgId) {
        return this.lambdaQuery().eq(Category::getOrgId, orgId).eq(Category::getId, categoryId).last("limit 1").one();
    }

    @Override
    public Category findOrCreate(String[] categoryNameArray, CategoryGroupEnum categoryGroupEnum) throws Exception {
        if (ArrayUtils.isEmpty(categoryNameArray)) {
            throw new IllegalParamsException("分类不能为空！");
        }
        if (categoryGroupEnum == null) {
            throw new IllegalParamsException("分类分组不能为空！");
        }

        Category parentCategory = null;
        Long parentId = null;
        for (String categoryName : categoryNameArray) {
            if (parentCategory != null) {
                parentId = parentCategory.getId();
            }
            Category category = this.selectOneByName(SecurityContextHolder.getOrgId(), CategoryGroupEnum.QUESTION.getId(), parentId, categoryName);
            if (category == null) {
                category = new Category();
                category.setName(categoryName);
                category.setGroupId(categoryGroupEnum.getId());
                if (parentCategory == null) {
                    parentId = 0L;
                    category.setParentId(parentId);
                    category.setLevel(1);
                    category.setParentCode("0");
                } else {
                    parentId = parentCategory.getId();
                    category.setParentId(parentId);
                    int level = parentCategory.getLevel() + 1;
                    if (level > categoryMaxLevel) {
                        throw new ServiceException("层级不能超过" + categoryMaxLevel + "级！");
                    }
                    category.setLevel(level);
                    category.setParentCode(parentCategory.getParentCode() + "," + parentId);
                }

                Integer maxSortNum = this.selectMaxSortNum(parentId);
                category.setSortNum(++maxSortNum);
                this.save(category);

            } else if (!category.getEnabled()) {
                throw new ServiceException(String.format("分类【%s】已禁用！", category.getName()));
            }

            parentCategory = category;
        }

        return parentCategory;
    }

    /**
     * 根据名称查找分类，如果个数大于1，则抛出异常
     * @param categoryName
     * @param orgId
     * @return
     * @throws ServiceException
     */
    @Override
    public Category selectOneByName(Long orgId, Integer groupId, Long parentId, String categoryName) throws ServiceException {
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为null！");
        }
        if (groupId == null) {
            throw new IllegalParamsException("groupId 不能为null！");
        }
        if (categoryName == null) {
            throw new IllegalParamsException("categoryName 不能为空！");
        }

        List<Category> categoryList = this.lambdaQuery().eq(Category::getOrgId, orgId).eq(Category::getGroupId, groupId)
                .eq(parentId != null && parentId > 0L, Category::getParentId, parentId)
                .eq(Category::getName, categoryName)
                .list();
        if (CollectionUtils.isEmpty(categoryList)) {
            return null;
        }

        if (categoryList.size() > 1) {
            throw new ServiceException(String.format("存在%d个同名分类【%s】！", categoryList.size(), categoryName));
        }

        return categoryList.get(0);
    }

    @Override
    public Long getCategoryQuestionCount(CategoryQuestionCountRequest param) {
        return this.getBaseMapper().selectQuestionCount(param);
    }

    @Override
    public Long getCategoryPaperCount(List<Long> categoryIdList) {
        return this.getBaseMapper().selectPaperCount(categoryIdList);
    }

}

