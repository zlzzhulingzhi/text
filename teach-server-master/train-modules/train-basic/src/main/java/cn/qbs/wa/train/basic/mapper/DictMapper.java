package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.Dict;
import cn.qbs.wa.train.basic.pojo.dict.DictDetailResponse;
import cn.qbs.wa.train.basic.pojo.dict.DictPageRequest;
import cn.qbs.wa.train.basic.pojo.dict.DictPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 字典表(Dict)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-08 13:32:05
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Dict> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Dict> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Dict> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Dict> entities);

    IPage<DictPageResponse> page(@Param("page") IPage<?> page, @Param("params") DictPageRequest params);

    DictDetailResponse selectDetailById(Serializable id);

    String getDictValue(@Param("params") DictPageRequest params);

    String getDictKey(@Param("params") DictPageRequest params);

    IPage<DictPageResponse> pageBycode(@Param("page") IPage<?> page, @Param("params") DictPageRequest params);

}

