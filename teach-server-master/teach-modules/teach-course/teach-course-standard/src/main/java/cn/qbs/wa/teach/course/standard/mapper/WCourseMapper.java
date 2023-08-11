package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.WCourse;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCourseDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageByChildResponse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.WCoursePageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 插件-课程表(WCourse)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
public interface WCourseMapper extends BaseMapper<WCourse> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<WCourse> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<WCourse> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<WCourse> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<WCourse> entities);

    @InterceptorIgnore(tenantLine = "true")
    IPage<WCoursePageResponse> page(@Param("page") IPage<?> page, @Param("params") WCoursePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<WCoursePageResponse> pageByLecturer(Page<?> page, @Param("params") WCoursePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<WCoursePageResponse> search(Page<?> mpPage, @Param("params") WCoursePageRequest params);

    WCourseDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    IPage<WCoursePageByChildResponse> pageByChild(Page<Object> mpPage, WCoursePageRequest params);
}

