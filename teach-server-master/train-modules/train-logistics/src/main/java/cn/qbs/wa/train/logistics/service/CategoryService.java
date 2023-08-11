package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.Category;
import cn.qbs.wa.train.logistics.pojo.category.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分类(Category)表服务接口
 *
 * @author makejava
 * @since 2022-01-18 09:48:40
 */
public interface CategoryService extends IService<Category> {

    /**
     * 新增通用分类
     * @param params
     * @return
     */
    boolean add(CategoryAddRequest params);

    /**
     * 分页查询通用分类
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
     * 更新通用分类
     * @param params
     * @return
     */
    boolean update(CategoryUpdateRequest params);

    /**
     * 删除通用分类
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<CategoryTreeResponse> tree(CategoryTreeRequest params);

    List<CategoryListResponse> childList(CategoryListRequest params);

    void batchEnabled(Integer flag, List<Long> idList);
}

