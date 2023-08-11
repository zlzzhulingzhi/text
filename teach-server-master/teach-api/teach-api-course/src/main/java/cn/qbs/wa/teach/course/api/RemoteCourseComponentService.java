package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseComponentFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.component.CourseComponentExtraDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseComponentService", name = "teach-course", path = "course/inner/courseComponent", fallbackFactory = RemoteCourseComponentFallbackFactory.class)
public interface RemoteCourseComponentService {

    @PostMapping("/v2/listByLesson")
    R<List<CourseComponentExtraDTO>> listByLessonV2(@RequestBody  IdRequest request);
}
