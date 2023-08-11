package cn.qbs.wa.train.basic.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.basic.entity.ClockInConfig;
import cn.qbs.wa.train.basic.pojo.clockinconfig.ClockInConfigDetailResponse;
import cn.qbs.wa.train.basic.pojo.clockinconfig.ClockInConfigPageRequest;
import cn.qbs.wa.train.basic.pojo.clockinconfig.ClockInConfigPageResponse;

/**
 * 打卡配置(ClockInConfig)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-26 15:38:20
 */
public interface ClockInConfigMapper extends BaseMapper<ClockInConfig> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<ClockInConfig> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<ClockInConfig> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<ClockInConfig> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<ClockInConfig> entities);

    IPage<ClockInConfigPageResponse> page(@Param("page") IPage<?> page, @Param("params") ClockInConfigPageRequest params);

    ClockInConfigDetailResponse selectDetailById(Serializable id);

}

