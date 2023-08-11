package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.standard.entity.TActivity;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.TActivityDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.TActivityPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.TActivityPageResponse;

/**
 * 活动表(TActivity)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-13 15:55:03
 */
public interface TActivityMapper extends BaseMapper<TActivity> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TActivity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TActivity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TActivity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TActivity> entities);

    IPage<TActivityPageResponse> page(@Param("page") IPage<?> page, @Param("params") TActivityPageRequest params);

    TActivityDetailResponse selectDetailById(Serializable id);

}

