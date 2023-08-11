package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.SystemApplication;
import cn.qbs.wa.union.auth.pojo.systemapplication.SystemApplicationDetailResponse;
import cn.qbs.wa.union.auth.pojo.systemapplication.SystemApplicationPageRequest;
import cn.qbs.wa.union.auth.pojo.systemapplication.SystemApplicationPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统应用】(SystemApplication)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:02:52
 */
public interface SystemApplicationMapper extends BaseMapper<SystemApplication> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemApplication> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemApplication> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemApplication> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemApplication> entities);

    IPage<SystemApplicationPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemApplicationPageRequest params);

    SystemApplicationDetailResponse selectDetailById(Serializable id);

}

