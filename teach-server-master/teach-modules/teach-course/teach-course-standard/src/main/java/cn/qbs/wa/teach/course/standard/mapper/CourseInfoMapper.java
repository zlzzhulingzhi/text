package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseInfo;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoPageResponse;

/**
 * 【课程信息】(CourseInfo)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
public interface CourseInfoMapper extends BaseMapper<CourseInfo> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CourseInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseInfo> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CourseInfo> entities);

    IPage<CourseInfoPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseInfoPageRequest params);

    CourseInfoDetailResponse selectDetailById(@Param("id") Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    CourseInfoDetailResponse selectDetailByCourseId(@Param("courseId") Long courseId);

    @InterceptorIgnore(tenantLine = "true")
    CourseInfo getByCourseId(@Param("courseId") Long courseId);

    @InterceptorIgnore(tenantLine = "true")
    void updateDurationByCourseId(@Param("courseId") Long courseId, @Param("courseDuration") Long courseDuration);

    CourseInfoDTO baseInfo(@Param("courseId") Long courseId);
}

