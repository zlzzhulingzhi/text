package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.ApplicationType;
import cn.qbs.wa.train.basic.pojo.applicationtype.ApplicationTypeDetailResponse;
import cn.qbs.wa.train.basic.pojo.applicationtype.ApplicationTypePageRequest;
import cn.qbs.wa.train.basic.pojo.applicationtype.ApplicationTypePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * (ApplicationType)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-09 19:14:26
 */
public interface ApplicationTypeMapper extends BaseMapper<ApplicationType> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<ApplicationType> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<ApplicationType> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<ApplicationType> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<ApplicationType> entities);

    IPage<ApplicationTypePageResponse> page(@Param("page") IPage<?> page, @Param("params") ApplicationTypePageRequest params);

    ApplicationTypeDetailResponse selectDetailById(Serializable id);

}

