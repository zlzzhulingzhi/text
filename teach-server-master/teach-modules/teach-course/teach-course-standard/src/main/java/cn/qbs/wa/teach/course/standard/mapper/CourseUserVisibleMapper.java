package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import cn.qbs.wa.teach.course.standard.pojo.courseuservisible.CourseUserVisibleDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseuservisible.CourseUserVisiblePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseuservisible.CourseUserVisiblePageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程可见用户】(CourseUserVisible)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
public interface CourseUserVisibleMapper extends BaseMapper<CourseUserVisible> {

    IPage<CourseUserVisiblePageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseUserVisiblePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseUserVisibleDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseUserVisible> listByCourseId(@Param("courseId") Long courseId);
}

