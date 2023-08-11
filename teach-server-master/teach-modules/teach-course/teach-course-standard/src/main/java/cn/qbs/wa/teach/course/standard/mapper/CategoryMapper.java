package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.category.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.Category;

/**
 * 【课程分类】(Category)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Category> 实例对象列表
    * @return 影响行数
    */
    @InterceptorIgnore(tenantLine = "true")
    int insertBatch(@Param("entities") List<Category> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Category> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Category> entities);

    /**
     * 分页查询接口
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<CategoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") CategoryPageRequest params);

    /**
     * 列表查询接口
     * @param params 查询参数
     * @return 查询列表
     */
    List<CategoryListResponse> list(@Param("params") CategoryListRequest params);

    /**
     * 查询分类详细信息
     * @param id 分类主键
     * @return 分类详细信息
     */
    @InterceptorIgnore(tenantLine = "true")
    CategoryDetailResponse selectDetailById(@Param("id") Serializable id);

}

