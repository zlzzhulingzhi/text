package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.train.logistics.pojo.classroom.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.Classroom;

/**
 * 教室表(Classroom)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-11 17:30:10
 */
public interface ClassroomMapper extends BaseMapper<Classroom> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Classroom> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Classroom> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Classroom> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Classroom> entities);

    IPage<ClassroomPageResponse> page(@Param("page") IPage<?> page, @Param("params") ClassroomPageRequest params);

    ClassroomDetailResponse selectDetailById(Long id);

    IPage<ClassroomSummaryResponse> getClassroomSummary(@Param("page") IPage<?> page, @Param("params") ClassroomSummaryRequest params);

    IPage<ClassroomPageResponse> pageV2(@Param("page") IPage<?> page, @Param("params") ClassroomSummaryRequest params);

    IPage<ClassroomPageResponse> pageInuse(IPage<?> page, @Param("params") ClassroomPageRequest params);

    IPage<ClassroomPageResponse> pageUnused(IPage<?> page, @Param("params") ClassroomPageRequest params);
}

