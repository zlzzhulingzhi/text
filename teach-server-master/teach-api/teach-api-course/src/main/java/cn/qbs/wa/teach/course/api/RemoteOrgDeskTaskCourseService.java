package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteOrgDeskTaskCourseFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.orgdesk.CourseInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WX
 */
@FeignClient(contextId = "remoteOrgDeskTaskCourseService", name = "teach-course", path = "course/orgDeskTask", fallbackFactory = RemoteOrgDeskTaskCourseFallbackFactory.class)
public interface RemoteOrgDeskTaskCourseService {

    /**
     * 课程信息
     * @param request ID
     * @return 课程内容
     */
    @PostMapping("getCourseList")
    R<List<CourseInfoDTO>> getCourseList(@RequestBody IdListAndUserIdRequest request);

}
