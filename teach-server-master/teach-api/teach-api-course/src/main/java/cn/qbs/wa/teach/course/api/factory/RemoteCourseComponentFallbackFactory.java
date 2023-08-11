package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseComponentService;
import cn.qbs.wa.teach.course.api.pojo.DTO.component.CourseComponentExtraDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yxj
 */
@Component
public class RemoteCourseComponentFallbackFactory implements FallbackFactory<RemoteCourseComponentService> {
    @Override
    public RemoteCourseComponentService create(Throwable cause) {
        return new RemoteCourseComponentService() {


            @Override
            public R<List<CourseComponentExtraDTO>> listByLessonV2(IdRequest request) {
                return null;
            }
        };
    }
}
