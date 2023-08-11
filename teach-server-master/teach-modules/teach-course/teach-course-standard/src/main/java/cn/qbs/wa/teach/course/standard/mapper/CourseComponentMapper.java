package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentExtraDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲次内容】(CourseComponent)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseComponentMapper extends BaseMapper<CourseComponent> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseComponent> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CourseComponent> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CourseComponent> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CourseComponent> entities);

    IPage<CourseComponentPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseComponentPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseComponentDetailResponse selectDetailById(@Param("id") Serializable id);

    List<CourseComponentDTO> listByLesson(@Param("lessonId") Long lessonId);

    List<CourseComponentExtraDTO> listByLessonV2(@Param("lessonId")Long lessonId);

    List<String> listTypeByCourseId(@Param("courseId") Long courseId);

}

