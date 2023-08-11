package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseStatisticOverview;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.CourseStatisticOverviewDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.CourseStatisticOverviewPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.CourseStatisticOverviewPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.EvaluationInfoDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程统计信息】(CourseStatisticOverview)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseStatisticOverviewMapper extends BaseMapper<CourseStatisticOverview> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseStatisticOverview> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CourseStatisticOverview> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseStatisticOverview> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CourseStatisticOverview> entities);

    IPage<CourseStatisticOverviewPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseStatisticOverviewPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseStatisticOverviewDetailResponse selectDetailById(@Param("id") Serializable id);

    /**
     * 增加评分信息
     *
     * @param evaluationInfo 评分信息
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    boolean incrEvaluationInfo(EvaluationInfoDTO evaluationInfo);

    /**
     * 增加购买人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int incrBuyerNum(@Param("courseId") Long courseId, @Param("num") int num);

    /**
     * 增加报名人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int incrSignUpNum(@Param("courseId") Long courseId, @Param("num") int num);

    /**
     * 增加学已完成人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int incrStudyCompletedNum(@Param("courseId") Long courseId, @Param("num") int num);

    /**
     * 增加学习人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int incrStudyTotalNum(@Param("courseId") Long courseId, @Param("num") int num);

    /**
     * 增加课时数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int incrLessonNum(@Param("courseId") Long courseId, @Param("num") int num);

    /**
     * 增加访问数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    @InterceptorIgnore(tenantLine = "true")
    int incrViews(@Param("courseId") Long courseId, @Param("num") int num);

    @InterceptorIgnore(tenantLine = "true")
    void refreshStatistic(@Param("courseId") Long courseId, @Param("overview") CourseStatisticOverview overview);

    @InterceptorIgnore(tenantLine = "true")
    CourseStatisticOverview totalCourseView(Long courseId);
}

