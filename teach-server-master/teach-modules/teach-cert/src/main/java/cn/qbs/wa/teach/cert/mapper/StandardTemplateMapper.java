package cn.qbs.wa.teach.cert.mapper;

import cn.qbs.wa.teach.cert.entity.StandardTemplate;
import cn.qbs.wa.teach.cert.pojo.standardtemplate.StandardTemplateDetailResponse;
import cn.qbs.wa.teach.cert.pojo.standardtemplate.StandardTemplatePageRequest;
import cn.qbs.wa.teach.cert.pojo.standardtemplate.StandardTemplatePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 证书模板(StandardTemplate)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-19 11:38:21
 */
public interface StandardTemplateMapper extends BaseMapper<StandardTemplate> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<StandardTemplate> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<StandardTemplate> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<StandardTemplate> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<StandardTemplate> entities);

    IPage<StandardTemplatePageResponse> page(@Param("page") IPage<?> page, @Param("params") StandardTemplatePageRequest params);

    StandardTemplateDetailResponse selectDetailById(Serializable id);

}

