package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.NewsCategory;
import cn.qbs.wa.train.logistics.pojo.newscategory.NewsCategoryDetailResponse;
import cn.qbs.wa.train.logistics.pojo.newscategory.NewsCategoryPageRequest;
import cn.qbs.wa.train.logistics.pojo.newscategory.NewsCategoryPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻-分类关系表(NewsCategory)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-18 09:30:04
 */
public interface NewsCategoryMapper extends BaseMapper<NewsCategory> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<NewsCategory> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<NewsCategory> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<NewsCategory> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<NewsCategory> entities);

    IPage<NewsCategoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") NewsCategoryPageRequest params);

    NewsCategoryDetailResponse selectDetailById(Serializable id);

}

