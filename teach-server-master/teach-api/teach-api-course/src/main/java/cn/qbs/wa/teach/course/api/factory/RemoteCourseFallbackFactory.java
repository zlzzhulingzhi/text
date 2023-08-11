package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.*;
import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDetailDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lesson.CourseLessonDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.student.CourseStudentLearnDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.student.SignUpDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yxj
 */
@Component
public class RemoteCourseFallbackFactory implements FallbackFactory<RemoteCourseService> {
    @Override
    public RemoteCourseService create(Throwable cause) {
        return new RemoteCourseService() {
            @Override
            public R<CourseInfoDetailDTO> detail(IdRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<CourseInfoDetailDTO> info(IdRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> signUp(SignUpDTO signUp, String source) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<CoursePageResultDTO>> page(CoursePageSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> dropOut(SignUpDTO dropOut) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<CourseStudentLearnDTO>> studentLearn(SignUpDTO request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<CourseListResultDTO>> listSelect(CourseListSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<CourseLecturerDetailDTO>> isRelevance(IdListRequest params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> updateCourseLecturer(CourseLecturerDTO params) {
                return null;
            }

            @Override
            public R<CourseDetailResponseDTO> getCourse(IdRequest request) {
                return null;
            }

            @Override
            public R<List<Long>> getIdList(CoursePageSearchDTO params) {
                return null;
            }

            @Override
            public R<List<CourseLessonDTO>> getCourseLesson(IdRequest request) {
                return null;
            }

            @Override
            public R<List<CoursePageResultDTO>> listByIds(IdListRequest params) {
                return null;
            }
        };
    }
}
