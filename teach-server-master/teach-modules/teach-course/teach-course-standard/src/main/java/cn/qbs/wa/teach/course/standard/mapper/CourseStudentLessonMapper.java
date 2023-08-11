package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson.CourseStudentLessonDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson.CourseStudentLessonPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson.CourseStudentLessonPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.LastLearnedDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【学员课程课次信息】(CourseStudentLesson)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-07 14:16:23
 */
public interface CourseStudentLessonMapper extends BaseMapper<CourseStudentLesson> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseStudentLesson> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseStudentLesson> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseStudentLesson> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseStudentLesson> entities);

    IPage<CourseStudentLessonPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseStudentLessonPageRequest params);

    CourseStudentLessonDetailResponse selectDetailById(Serializable id);

    List<CourseStudentLesson> getUsefulCourseAllLesson(@Param("userId")Long userId, @Param("courseId") Long courseId);

    LastLearnedDTO selectBusinessName(@Param("userId")Long userId, @Param("courseId") Long courseId, @Param("componentId") Long componentId);


}

