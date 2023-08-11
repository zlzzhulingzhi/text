package cn.qbs.wa.teach.course.standard.mapper;

import java.time.LocalDateTime;
import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyListRequest;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseStatisticDaily;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyPageResponse;

/**
 * 【课程学习每日统计】(CourseStatisticDaily)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseStatisticDailyMapper extends BaseMapper<CourseStatisticDaily> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseStatisticDaily> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CourseStatisticDaily> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseStatisticDaily> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CourseStatisticDaily> entities);

    IPage<CourseStatisticDailyPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseStatisticDailyPageRequest params);

    CourseStatisticDailyDetailResponse selectDetailById(Serializable id);

    List<CourseStatisticDailyPageResponse> list(@Param("params") CourseStatisticDailyListRequest params);

    @InterceptorIgnore(tenantLine = "true")
    int countStudentNumByCourseId(@Param("courseId") Long courseId, @Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
}

