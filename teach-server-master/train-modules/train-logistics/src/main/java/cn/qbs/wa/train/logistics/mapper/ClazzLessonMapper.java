package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.ClazzLesson;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.ClazzLessonDetailResponse;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.ClazzLessonPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.ClazzLessonPageResponse;

/**
 * 班级课程(ClazzLesson)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-13 20:12:56
 */
public interface ClazzLessonMapper extends BaseMapper<ClazzLesson> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClazzLesson> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ClazzLesson> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClazzLesson> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ClazzLesson> entities);

    IPage<ClazzLessonPageResponse> page(@Param("page") IPage<?> page, @Param("params") ClazzLessonPageRequest params);

    ClazzLessonDetailResponse selectDetailById(Serializable id);

}

