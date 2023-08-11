package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.Menu;
import cn.qbs.wa.train.basic.pojo.menu.MenuDetailResponse;
import cn.qbs.wa.train.basic.pojo.menu.MenuPageRequest;
import cn.qbs.wa.train.basic.pojo.menu.MenuPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统菜单】(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-04 16:28:06
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Menu> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Menu> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Menu> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Menu> entities);

    IPage<MenuPageResponse> page(@Param("page") IPage<?> page, @Param("params") MenuPageRequest params);

    MenuDetailResponse selectDetailById(Serializable id);

    List<Menu> listMenuByAppType(Long appTypeId);
}

