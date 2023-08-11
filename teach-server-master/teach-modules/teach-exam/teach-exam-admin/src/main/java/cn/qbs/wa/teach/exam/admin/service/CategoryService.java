package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.exam.admin.pojo.category.*;
import cn.qbs.wa.teach.exam.common.entity.Category;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 分类(Category)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:34:12
 */
public interface CategoryService extends IService<Category> {
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

    boolean enable(EnableRequest request);

    List<?> getTreeList();

    List<CategoryPageResponse> childrenList(Long parentId);

}


