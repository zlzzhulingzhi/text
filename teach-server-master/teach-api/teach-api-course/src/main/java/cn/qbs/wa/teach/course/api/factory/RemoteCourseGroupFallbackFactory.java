package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseGroupService;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup.CourseGroupAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup.CourseGroupDetailResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WX
 */
@Component
public class RemoteCourseGroupFallbackFactory implements FallbackFactory<RemoteCourseGroupService> {
    @Override
    public RemoteCourseGroupService create(Throwable cause) {
        return new RemoteCourseGroupService() {
            @Override
            public R<Boolean> addCourseStudentByGroup(List<CourseGroupAddRequestDTO> requests) {
                return null;
            }

            @Override
            public R<List<CourseGroupDetailResponseDTO>> listByGroupId(IdListRequest request) {
                return null;
            }
        };
    }
}
