package cn.qbs.wa.teach.cert.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.cert.entity.StandardTemplateConfig;
import cn.qbs.wa.teach.cert.pojo.standardtemplateconfig.StandardTemplateConfigDetailResponse;
import cn.qbs.wa.teach.cert.pojo.standardtemplateconfig.StandardTemplateConfigPageRequest;
import cn.qbs.wa.teach.cert.pojo.standardtemplateconfig.StandardTemplateConfigPageResponse;

/**
 * 证书模板配置(StandardTemplateConfig)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
public interface StandardTemplateConfigMapper extends BaseMapper<StandardTemplateConfig> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<StandardTemplateConfig> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<StandardTemplateConfig> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<StandardTemplateConfig> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<StandardTemplateConfig> entities);

    IPage<StandardTemplateConfigPageResponse> page(@Param("page") IPage<?> page, @Param("params") StandardTemplateConfigPageRequest params);

    StandardTemplateConfigDetailResponse selectDetailById(Serializable id);

}

