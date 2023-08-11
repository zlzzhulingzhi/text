package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.SystemEmployeeUser;
import cn.qbs.wa.union.auth.pojo.systememployeeuser.SystemEmployeeUserDetailResponse;
import cn.qbs.wa.union.auth.pojo.systememployeeuser.SystemEmployeeUserPageRequest;
import cn.qbs.wa.union.auth.pojo.systememployeeuser.SystemEmployeeUserPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 职工用户表(SystemEmployeeUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:03
 */
public interface SystemEmployeeUserMapper extends BaseMapper<SystemEmployeeUser> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemEmployeeUser> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemEmployeeUser> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemEmployeeUser> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemEmployeeUser> entities);

    IPage<SystemEmployeeUserPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemEmployeeUserPageRequest params);

    SystemEmployeeUserDetailResponse selectDetailById(Serializable id);

}

