package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.Unit;
import cn.qbs.wa.train.basic.pojo.unit.UnitDetailResponse;
import cn.qbs.wa.train.basic.pojo.unit.UnitPageRequest;
import cn.qbs.wa.train.basic.pojo.unit.UnitPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 单位表(Unit)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-29 08:31:21
 */
public interface UnitMapper extends BaseMapper<Unit> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Unit> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Unit> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Unit> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Unit> entities);

    IPage<UnitPageResponse> page(@Param("page") IPage<?> page, @Param("params") UnitPageRequest params);

    UnitDetailResponse selectDetailById(Serializable id);

}

