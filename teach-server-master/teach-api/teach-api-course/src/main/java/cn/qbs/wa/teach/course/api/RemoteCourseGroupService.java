package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseGroupFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup.CourseGroupAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup.CourseGroupDetailResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WX
 */
@FeignClient(contextId = "remoteCourseGroupService", name = "teach-course", path = "course/courseGroup", fallbackFactory = RemoteCourseGroupFallbackFactory.class)
public interface RemoteCourseGroupService {

    @PostMapping("/addCourseStudentByGroup")
    @ApiOperation("根据标签id添加课程学员")
    R<Boolean> addCourseStudentByGroup(@RequestBody List<CourseGroupAddRequestDTO> requests);

    @PostMapping("/listByGroupId")
    @ApiOperation("根据标签id获取课程标签列表")
    R<List<CourseGroupDetailResponseDTO>> listByGroupId(@RequestBody IdListRequest request);
}
