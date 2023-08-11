package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemotePlatformCourseFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageByLecturerResultDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageResultDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageSearchDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.LecturerCourseRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yjx
 */
@FeignClient(contextId = "remotePlatformCourseService", name = "teach-course", path = "course/platform", fallbackFactory = RemotePlatformCourseFallbackFactory.class)
public interface RemotePlatformCourseService {

    /**
     * 分页查询【课程】
     */
    @ApiOperation(value = "课程分页查询")
    @PostMapping("/page")
    R<PageResultComDTO<CoursePageResultDTO>> page(@RequestBody CoursePageSearchDTO params);

    /**
     * 分页查询【课程】
     */
    @ApiOperation(value = "平台管理-课程选择器")
    @PostMapping("admin/page")
    R<PageResultComDTO<CoursePageResultDTO>> pageAdmin(@RequestBody CoursePageSearchDTO params);

    /**
     * 根据讲师id分页查询【课程】
     */
    @ApiOperation(value = "讲师的课程分页查询")
    @PostMapping("/lectureCoursePage")
    R<PageResultComDTO<CoursePageByLecturerResultDTO>> lectureCoursePage(@RequestBody LecturerCourseRequestDTO params);

}
