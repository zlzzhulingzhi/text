package cn.qbs.wa.teach.basic.mapper;

import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.basic.pojo.role.RolePageRequest;
import cn.qbs.wa.teach.basic.pojo.role.RolePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【系统角色】(Role)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Role> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Role> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Role> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Role> entities);

    IPage<RolePageResponse> pageRole(Page<Role> page, RolePageRequest request);
}

