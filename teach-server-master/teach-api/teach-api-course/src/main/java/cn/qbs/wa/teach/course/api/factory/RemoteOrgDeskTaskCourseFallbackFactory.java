package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteOrgDeskTaskCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.orgdesk.CourseInfoDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WX
 */
@Component
public class RemoteOrgDeskTaskCourseFallbackFactory implements FallbackFactory<RemoteOrgDeskTaskCourseService> {
    @Override
    public RemoteOrgDeskTaskCourseService create(Throwable cause) {
        return new RemoteOrgDeskTaskCourseService() {
            @Override
            public R<List<CourseInfoDTO>> getCourseList(IdListAndUserIdRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
