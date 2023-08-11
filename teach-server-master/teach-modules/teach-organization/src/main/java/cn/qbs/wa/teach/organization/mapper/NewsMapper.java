package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.entity.News;
import cn.qbs.wa.teach.organization.pojo.news.NewsDetailResponse;
import cn.qbs.wa.teach.organization.pojo.news.NewsPageRequest;
import cn.qbs.wa.teach.organization.pojo.news.NewsPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 新闻(News)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-18 11:30:45
 */
public interface NewsMapper extends BaseMapper<News> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<News> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<News> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<News> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<News> entities);

    IPage<NewsPageResponse> page(@Param("page") IPage<?> page, @Param("params") NewsPageRequest params);

    NewsDetailResponse selectDetailById(Serializable id);

}

