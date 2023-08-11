package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.ApplicationApplicationType;
import cn.qbs.wa.train.basic.pojo.applicationapplicationtype.ApplicationApplicationTypeDetailResponse;
import cn.qbs.wa.train.basic.pojo.applicationapplicationtype.ApplicationApplicationTypePageRequest;
import cn.qbs.wa.train.basic.pojo.applicationapplicationtype.ApplicationApplicationTypePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

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

