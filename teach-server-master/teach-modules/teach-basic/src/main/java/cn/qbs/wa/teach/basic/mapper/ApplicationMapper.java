package cn.qbs.wa.teach.basic.mapper;

import cn.qbs.wa.teach.basic.entity.Application;
import cn.qbs.wa.teach.basic.pojo.app.ApplicationFullResponse;
import cn.qbs.wa.teach.basic.pojo.app.ApplicationPageRequest;
import cn.qbs.wa.teach.basic.pojo.app.ApplicationPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Application)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-02 14:55:25
 */
public interface ApplicationMapper extends BaseMapper<Application> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Application> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Application> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Application> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Application> entities);

    List<ApplicationFullResponse > getChildrenByAppTypeId(Long applicationTypeId);

    IPage<ApplicationPageResponse> page(Page<ApplicationPageResponse> page, ApplicationPageRequest request);
}

