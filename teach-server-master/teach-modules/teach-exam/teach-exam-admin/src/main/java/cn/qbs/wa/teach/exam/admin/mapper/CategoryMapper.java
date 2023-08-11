package cn.qbs.wa.teach.exam.admin.mapper;

import cn.qbs.wa.teach.exam.admin.pojo.category.CategoryDetailResponse;
import cn.qbs.wa.teach.exam.admin.pojo.category.CategoryPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.category.CategoryPageResponse;
import cn.qbs.wa.teach.exam.common.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 分类(Category)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:34:12
 */
public interface CategoryMapper extends BaseMapper<Category> {

    IPage<CategoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") CategoryPageRequest params);

    CategoryDetailResponse selectDetailById(Serializable id);

    List<CategoryPageResponse> childrenList(@Param("parentId") Long parentId);

    Integer selectMaxSortNum(Long parentCategoryId);



}

