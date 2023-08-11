package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.Category;
import cn.qbs.wa.teach.course.standard.pojo.category.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程分类】(Category)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface CategoryService extends IService<Category> {

    Category init(CategoryAddRequest params);

    /**
     * 新增【课程分类】
     * @param params
     * @return
     */
    Category add(CategoryAddRequest params);

    /**
     * 分页查询【课程分类】
     * @param params
     * @return
     */
    IPage<CategoryPageResponse> page(CategoryPageRequest params);

    /**
     *【课程分类】列表
     * @param params
     * @return
     */
    List<CategoryListResponse> list(CategoryListRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CategoryDetailResponse detail(Serializable id);

    /**
     * 更新【课程分类】
     * @param params
     * @return
     */
    boolean update(CategoryUpdateRequest params);

    /**
     * 删除【课程分类】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 树形结构
     * @param params 参数
     * @return 树形结构
     */
    List<CategoryTreeResponse> tree(CategoryTreeRequest params);

    /**
     * 是否存在子分类
     * @param id 分类ID
     * @return true 存在，false：不存在
     */
    boolean hasChildren(Long id);

    /**
     * 树形结构
     * @param params 参数
     * @return 树形结构
     */
    List<CategoryTreeResponse> treeV2(CategoryTreeRequest params);

    boolean enable(List<Long> categoryIdList, Integer enabled);

}

