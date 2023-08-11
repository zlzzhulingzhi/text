package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseLecturerService;
import cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture.CourseLecturerPageRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture.CourseLecturerQueryDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author yxj
 */
@Component
public class RemoteCourseLecturerFallbackFactory implements FallbackFactory<RemoteCourseLecturerService> {
    @Override
    public RemoteCourseLecturerService create(Throwable cause) {
        return new RemoteCourseLecturerService() {

            @Override
            public R<Long> count(CourseLecturerPageRequestDTO request) {
                return null;
            }

            @Override
            public R<List<Long>> queryLecturerIds(CourseLecturerQueryDTO lecturerQuery) {
                return null;
            }
        };
    }
}
