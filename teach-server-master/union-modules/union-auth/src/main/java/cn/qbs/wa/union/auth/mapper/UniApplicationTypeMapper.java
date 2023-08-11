package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.UniApplicationType;
import cn.qbs.wa.union.auth.pojo.uniapplicationtype.UniApplicationTypeDetailResponse;
import cn.qbs.wa.union.auth.pojo.uniapplicationtype.UniApplicationTypePageRequest;
import cn.qbs.wa.union.auth.pojo.uniapplicationtype.UniApplicationTypePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用类型(UniApplicationType)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
public interface UniApplicationTypeMapper extends BaseMapper<UniApplicationType> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplicationType> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UniApplicationType> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplicationType> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UniApplicationType> entities);

    IPage<UniApplicationTypePageResponse> page(@Param("page") IPage<?> page, @Param("params") UniApplicationTypePageRequest params);

    UniApplicationTypeDetailResponse selectDetailById(Serializable id);

}

