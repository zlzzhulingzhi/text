package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CategoryDistribution;
import cn.qbs.wa.teach.course.standard.pojo.categorydistribution.CategoryDistributionDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.categorydistribution.CategoryDistributionPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.categorydistribution.CategoryDistributionPageResponse;

/**
 * 【课程分类分布】(CategoryDistribution)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface CategoryDistributionMapper extends BaseMapper<CategoryDistribution> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CategoryDistribution> 实例对象列表
     * @return 影响行数
     */
    @InterceptorIgnore(tenantLine = "true")
    int insertBatch(@Param("entities") List<CategoryDistribution> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CategoryDistribution> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CategoryDistribution> entities);

    IPage<CategoryDistributionPageResponse> page(@Param("page") IPage<?> page, @Param("params") CategoryDistributionPageRequest params);

    CategoryDistributionDetailResponse selectDetailById(Serializable id);

    /**
     * 课程分类下的课程数加一
     *
     * @param categoryId 课程分类ID
     * @param count 数量
     * @return 影响行数
     */
    int incrCourseCount(@Param("categoryId") Long categoryId, @Param("count") int count);

    /**
     * 课程分类下的课程数减一
     *
     * @param categoryId 课程分类ID
     * @param count 数量
     * @return 影响行数
     */
    int decrCourseCount(@Param("categoryId") Long categoryId, @Param("count") int count);
}

