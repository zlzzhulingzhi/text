package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【平台用户角色关联关系】(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-02 15:48:21
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UserRole> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UserRole> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UserRole> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UserRole> entities);

    List<Role> listRoleByUserId(Long userId);
}

