package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseChapter;
import cn.qbs.wa.teach.course.standard.pojo.coursechapter.CourseChapterDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursechapter.CourseChapterPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursechapter.CourseChapterPageResponse;

/**
 * 【课程章节】(CourseChapter)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseChapterMapper extends BaseMapper<CourseChapter> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseChapter> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseChapter> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseChapter> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseChapter> entities);

    IPage<CourseChapterPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseChapterPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseChapterDetailResponse selectDetailById(@Param("id") Serializable id);

    boolean incrLessonNumById(@Param("chapterId") Long chapterId, @Param("count") int count);

    boolean decrLessonNumById(@Param("chapterId") Long chapterId, @Param("count") int count);
}

