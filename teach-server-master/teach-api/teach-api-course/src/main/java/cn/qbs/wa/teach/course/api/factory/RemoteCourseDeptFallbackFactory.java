package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseDeptService;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursedept.CourseDeptAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursedept.CourseDeptDetailResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author WX
 */
@Component
public class RemoteCourseDeptFallbackFactory implements FallbackFactory<RemoteCourseDeptService> {
    @Override
    public RemoteCourseDeptService create(Throwable cause) {
        return new RemoteCourseDeptService() {
            @Override
            public R<Boolean> addCourseStudentByDept(List<CourseDeptAddRequestDTO> requests) {
                return null;
            }

            @Override
            public R<List<CourseDeptDetailResponseDTO>> listByDeptId(IdRequest request) {
                return null;
            }
        };
    }
}
