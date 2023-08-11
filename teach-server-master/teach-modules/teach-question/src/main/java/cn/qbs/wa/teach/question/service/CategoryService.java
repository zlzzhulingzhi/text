package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.entity.Category;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.pojo.SelectOption;
import cn.qbs.wa.teach.question.pojo.category.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 分类(Category)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:38:35
 */
public interface CategoryService extends IService<Category> {

    Category init(QuestionCategoryAddRequest params);
    /**
     * 新增分类
     * @param params
     * @return
     */
    Long add(CategoryAddRequest params);

    /**
     * 分页查询分类
     * @param params
     * @return
     */
    IPage<CategoryPageResponse> page(CategoryPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CategoryDetailResponse detail(Serializable id);

    /**
     * 更新分类
     * @param params
     * @return
     */
    boolean update(CategoryUpdateRequest params);

    void updateChildren(Category category);

    /**
     * 删除分类
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    boolean enable(List<Long> categoryIdList, Integer enabled);

    List<SelectOption> getSelectOptionList();

    List<CategoryTreeNode> getTreeList(Integer groupId, Boolean enabled);

    List<Category> getByGroupId(Integer groupId, Boolean enabled);

    void updateCache(Category category);

    List<CategoryPageResponse> childrenList(Long parentId);

    Category selectFirst(Long categoryId, Long orgId);

    Category findOrCreate(String[] categoryNameArray, CategoryGroupEnum categoryGroupEnum) throws Exception;

    /**
     * 根据名称查找分类，如果个数大于1，则抛出异常
     * @param orgId
     * @param groupId
     * @param parentId
     * @param categoryName
     * @return
     * @throws ServiceException
     */
    Category selectOneByName(Long orgId, Integer groupId, Long parentId, String categoryName) throws ServiceException;

    /**
     * 查询分类下面的试题数量
     * @param param
     * @return
     */
    Long getCategoryQuestionCount(CategoryQuestionCountRequest param);

    /**
     * 查询分类下面的试卷数量
     * @param categoryIdList
     * @return
     */
    Long getCategoryPaperCount(List<Long> categoryIdList);

}

