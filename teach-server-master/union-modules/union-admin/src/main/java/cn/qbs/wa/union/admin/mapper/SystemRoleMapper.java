package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.pojo.systemrole.SystemRoleDetailResponse;
import cn.qbs.wa.union.admin.pojo.systemrole.SystemRolePageRequest;
import cn.qbs.wa.union.admin.pojo.systemrole.SystemRolePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统角色】(SystemRole)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemRole> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemRole> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemRole> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemRole> entities);

    IPage<SystemRolePageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemRolePageRequest params);

    SystemRoleDetailResponse selectDetailById(Serializable id);

}

