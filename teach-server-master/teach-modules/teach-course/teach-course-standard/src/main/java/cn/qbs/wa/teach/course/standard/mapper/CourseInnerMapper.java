package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoDetailResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yjx
 */
@InterceptorIgnore(tenantLine = "true")
public interface CourseInnerMapper {

    Course getById(Long courseId);

    CourseInfoDetailResponse courseInfo(Long courseId);

    List<CoursePageResponse> getCourseList(@Param("params") CoursePageRequest params);

    List<Course> listByIds(@Param("courseIds") List<Long> courseIds);

    List<CourseLesson> getCourseLesson(Long courseId);
}
