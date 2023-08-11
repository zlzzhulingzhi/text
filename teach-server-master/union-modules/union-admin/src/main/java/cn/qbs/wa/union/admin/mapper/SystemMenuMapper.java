package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.SystemMenu;
import cn.qbs.wa.union.admin.pojo.systemmenu.SystemMenuDetailResponse;
import cn.qbs.wa.union.admin.pojo.systemmenu.SystemMenuPageRequest;
import cn.qbs.wa.union.admin.pojo.systemmenu.SystemMenuPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统菜单】(SystemMenu)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:04
 */
public interface SystemMenuMapper extends BaseMapper<SystemMenu> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemMenu> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemMenu> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemMenu> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemMenu> entities);

    IPage<SystemMenuPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemMenuPageRequest params);

    SystemMenuDetailResponse selectDetailById(Serializable id);

}

