package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.NavigationItem;
import cn.qbs.wa.train.basic.pojo.navigationitem.NavigationItemDetailResponse;
import cn.qbs.wa.train.basic.pojo.navigationitem.NavigationItemPageRequest;
import cn.qbs.wa.train.basic.pojo.navigationitem.NavigationItemPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【导航栏项】(NavigationItem)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 13:55:36
 */
public interface NavigationItemMapper extends BaseMapper<NavigationItem> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<NavigationItem> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<NavigationItem> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<NavigationItem> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<NavigationItem> entities);

    IPage<NavigationItemPageResponse> page(@Param("page") IPage<?> page, @Param("params") NavigationItemPageRequest params);

    NavigationItemDetailResponse selectDetailById(Serializable id);
    
}

