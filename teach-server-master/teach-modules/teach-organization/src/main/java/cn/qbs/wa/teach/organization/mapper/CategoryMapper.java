package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.entity.Category;
import cn.qbs.wa.teach.organization.pojo.category.CategoryDetailResponse;
import cn.qbs.wa.teach.organization.pojo.category.CategoryPageRequest;
import cn.qbs.wa.teach.organization.pojo.category.CategoryPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分类(Category)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-18 09:48:40
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Category> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Category> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Category> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Category> entities);

    IPage<CategoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") CategoryPageRequest params);

    CategoryDetailResponse selectDetailById(Serializable id);

}

