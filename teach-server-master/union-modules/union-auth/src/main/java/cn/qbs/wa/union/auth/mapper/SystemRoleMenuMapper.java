package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.SystemRoleMenu;
import cn.qbs.wa.union.auth.pojo.systemrolemenu.SystemRoleMenuDetailResponse;
import cn.qbs.wa.union.auth.pojo.systemrolemenu.SystemRoleMenuPageRequest;
import cn.qbs.wa.union.auth.pojo.systemrolemenu.SystemRoleMenuPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(SystemRoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
public interface SystemRoleMenuMapper extends BaseMapper<SystemRoleMenu> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemRoleMenu> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemRoleMenu> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemRoleMenu> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemRoleMenu> entities);

    IPage<SystemRoleMenuPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemRoleMenuPageRequest params);

    SystemRoleMenuDetailResponse selectDetailById(Serializable id);

}

