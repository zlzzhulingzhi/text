package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseStudentFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent.CourseStudentDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent.CourseStudentQueryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseStudentService", name = "teach-course", path = "course/inner", fallbackFactory = RemoteCourseStudentFallbackFactory.class)
public interface RemoteCourseStudentService {

    @PostMapping("/getCourseStudent")
    R<List<CourseStudentDTO>> getCourseStudent(@RequestBody IdRequest request);

    /**
     * 根据学员用户ID数组、课程名称过滤符合条件的 memberIds
     */
    @PostMapping("/queryStudent")
    R<List<Long>> queryStudent(@RequestBody CourseStudentQueryDTO courseStudentQueryDTO);
}
