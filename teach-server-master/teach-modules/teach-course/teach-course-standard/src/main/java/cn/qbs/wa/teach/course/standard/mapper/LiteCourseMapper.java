package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.MyCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.MyCoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.PageCourseVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiteCourseMapper {

    @InterceptorIgnore(tenantLine = "true")
    IPage<MyCoursePageResponse> pageMyCourse(Page<?> mpPage, @Param("params") MyCoursePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<PageCourseVO> pageCourse(Page<?> page, @Param("params") PageCourseDTO params);

    @InterceptorIgnore(tenantLine = "true")
    CourseInfoDTO selectDetailById(Long id);

    IPage<PageCourseVO> orgPageCourse(Page<?> page, @Param("params") PageCourseDTO params);

    List<CourseInfoDTO> listCourseLecturer(@Param("ids") List<Long> ids);
}
