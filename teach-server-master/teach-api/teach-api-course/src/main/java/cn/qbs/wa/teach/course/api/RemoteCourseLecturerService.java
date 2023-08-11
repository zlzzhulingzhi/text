package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseFallbackFactory;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseLecturerFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture.CourseLecturerPageRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture.CourseLecturerQueryDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseLecturerService", name = "teach-course", path = "course/inner/courseLecturer", fallbackFactory = RemoteCourseLecturerFallbackFactory.class)
public interface RemoteCourseLecturerService {

    @ApiOperation("获取讲师与课程关联数量")
    @PostMapping("count")
    R<Long> count(@RequestBody CourseLecturerPageRequestDTO request);

    @PostMapping("/queryLecturerIds")
    R<List<Long>> queryLecturerIds(@RequestBody CourseLecturerQueryDTO lecturerQuery);
}
