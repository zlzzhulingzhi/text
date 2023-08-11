package cn.qbs.wa.teach.basic.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.basic.entity.ApplicationApplicationType;
import cn.qbs.wa.teach.basic.pojo.applicationapplicationtype.ApplicationApplicationTypeDetailResponse;
import cn.qbs.wa.teach.basic.pojo.applicationapplicationtype.ApplicationApplicationTypePageRequest;
import cn.qbs.wa.teach.basic.pojo.applicationapplicationtype.ApplicationApplicationTypePageResponse;

/**
 * (ApplicationApplicationType)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-10 10:22:56
 */
public interface ApplicationApplicationTypeMapper extends BaseMapper<ApplicationApplicationType> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<ApplicationApplicationType> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<ApplicationApplicationType> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<ApplicationApplicationType> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<ApplicationApplicationType> entities);

    IPage<ApplicationApplicationTypePageResponse> page(@Param("page") IPage<?> page, @Param("params") ApplicationApplicationTypePageRequest params);

    ApplicationApplicationTypeDetailResponse selectDetailById(Serializable id);

}

