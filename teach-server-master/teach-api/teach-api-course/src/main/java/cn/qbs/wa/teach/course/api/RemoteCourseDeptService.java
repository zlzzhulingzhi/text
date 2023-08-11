package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseDeptFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursedept.CourseDeptAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursedept.CourseDeptDetailResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WX
 */
@FeignClient(contextId = "remoteCourseDeptService", name = "teach-course", path = "course/courseDept", fallbackFactory = RemoteCourseDeptFallbackFactory.class)
public interface RemoteCourseDeptService {

    @PostMapping("/addCourseStudentByDept")
    @ApiOperation("根据部门id添加课程学员")
    R<Boolean> addCourseStudentByDept(@RequestBody List<CourseDeptAddRequestDTO> requests);

    @PostMapping("/listByDeptId")
    @ApiOperation("根据部门id获取课程部门列表")
    R<List<CourseDeptDetailResponseDTO>> listByDeptId(@RequestBody IdRequest request);
}
