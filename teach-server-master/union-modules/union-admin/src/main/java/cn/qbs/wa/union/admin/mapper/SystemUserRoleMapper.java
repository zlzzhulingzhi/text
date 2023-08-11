package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.entity.SystemUserRole;
import cn.qbs.wa.union.admin.pojo.systemuserrole.SystemUserRoleDetailResponse;
import cn.qbs.wa.union.admin.pojo.systemuserrole.SystemUserRolePageRequest;
import cn.qbs.wa.union.admin.pojo.systemuserrole.SystemUserRolePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【平台用户角色关联关系】(SystemUserRole)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
public interface SystemUserRoleMapper extends BaseMapper<SystemUserRole> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemUserRole> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemUserRole> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemUserRole> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemUserRole> entities);

    IPage<SystemUserRolePageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemUserRolePageRequest params);

    SystemUserRoleDetailResponse selectDetailById(Serializable id);

    List<SystemRole> listRoleByUserId(Long userId);
}

