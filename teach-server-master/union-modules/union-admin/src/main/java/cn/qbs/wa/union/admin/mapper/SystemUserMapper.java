package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.SystemUser;
import cn.qbs.wa.union.admin.pojo.systemuser.SystemUserDetailResponse;
import cn.qbs.wa.union.admin.pojo.systemuser.SystemUserPageRequest;
import cn.qbs.wa.union.admin.pojo.systemuser.SystemUserPageResponse;
import cn.qbs.wa.union.admin.pojo.systemuser.SystemUserRoleMenuDataResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 平台系统子用户表(SystemUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemUser> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemUser> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemUser> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemUser> entities);

    IPage<SystemUserPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemUserPageRequest params);

    SystemUserDetailResponse selectDetailById(Serializable id);

    List<SystemUserRoleMenuDataResponse> getMinePermission(Long userId);

    List<SystemUser> listAdmin(String account);
}

