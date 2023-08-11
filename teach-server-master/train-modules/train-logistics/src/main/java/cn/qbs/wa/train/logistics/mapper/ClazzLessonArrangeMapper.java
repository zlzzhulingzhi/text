package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.train.logistics.pojo.clazz.IntegrateClazzResponse;
import cn.qbs.wa.train.logistics.pojo.clazzlessonarrange.*;
import cn.qbs.wa.train.logistics.pojo.lecturer.LecturerClazzMap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;

/**
 * 班级课程安排(ClazzLessonArrange)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-14 09:21:37
 */
public interface ClazzLessonArrangeMapper extends BaseMapper<ClazzLessonArrange> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClazzLessonArrange> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ClazzLessonArrange> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClazzLessonArrange> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ClazzLessonArrange> entities);

    IPage<ClazzLessonArrangePageResponse> page(@Param("page") IPage<?> page, @Param("params") ClazzLessonArrangePageRequest params);

    ClazzLessonArrangeDetailResponse selectDetailById(Serializable id);

    List<LecturerClazzMap> queryClazzLastByLecturerIds(@Param("lecturerIds") List<Long> lecturerIds);

    List<IntegrateClazzResponse> listClazzByLecturerId(Long lecturerId);

    List<ClazzLessonArrangeListResponse> listV2(@Param("params") ClazzLessonArrangePageRequest params);

    List<ClazzLessonArrangeListResponse> listV3(@Param("params") ClazzLessonArrangePageRequest params);

    ClazzLessonArrangeWeekResponse getWeek(@Param("params") ClazzLessonArrangePageRequest params);
}

