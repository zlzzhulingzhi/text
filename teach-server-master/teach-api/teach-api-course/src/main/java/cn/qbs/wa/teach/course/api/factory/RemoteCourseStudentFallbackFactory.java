package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.RemoteCourseStudentService;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent.CourseStudentDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent.CourseStudentQueryDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yxj
 */
@Component
public class RemoteCourseStudentFallbackFactory implements FallbackFactory<RemoteCourseStudentService> {
    @Override
    public RemoteCourseStudentService create(Throwable cause) {
        return new RemoteCourseStudentService() {

            @Override
            public R<List<CourseStudentDTO>> getCourseStudent(IdRequest request) {
                return null;
            }

            @Override
            public R<List<Long>> queryStudent(CourseStudentQueryDTO courseStudentQueryDTO) {
                return null;
            }
        };
    }
}
