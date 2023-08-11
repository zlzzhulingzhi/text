package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCoursePersonalService;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseDetailResponseDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseLessonSimpleResponseDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.LiveCourseStudyingDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.MyCourseDetailRequestDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yxj
 */
@Component
public class RemoteCoursePersonalFallbackFactory implements FallbackFactory<RemoteCoursePersonalService> {


    @Override
    public RemoteCoursePersonalService create(Throwable cause) {
        return new RemoteCoursePersonalService() {
            @Override
            public R<List<CourseLessonSimpleResponseDTO>> detail(List<MyCourseDetailRequestDTO> params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> liveRecording(List<LiveCourseStudyingDTO> params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<CourseDetailResponseDTO>> info(List<MyCourseDetailRequestDTO> params) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
