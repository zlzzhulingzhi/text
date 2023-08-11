package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.*;
import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDetailDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lesson.CourseLessonDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.student.CourseStudentLearnDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.student.SignUpDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseService", name = "teach-course", path = "course", fallbackFactory = RemoteCourseFallbackFactory.class)
public interface RemoteCourseService {

    /**
     * 展示课程内容
     * @param request ID
     * @return 课程内容
     */
    @PostMapping("/info")
    R<CourseInfoDetailDTO> detail(@RequestBody IdRequest request);

    /**
     * 展示课程信息
     * @param request ID
     * @return 课程信息
     */
    @PostMapping("/info/detail")
    R<CourseInfoDetailDTO> info(@RequestBody IdRequest request);

    /**
     *  报名
     * @param signUp 报名参数
     * @return 是否成功
     */
    @PostMapping("/student/add")
    R<Boolean> signUp(@RequestBody SignUpDTO signUp, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 分页查询【课程】
     */
    @PostMapping("/page")
    R<PageResultComDTO<CoursePageResultDTO>> page(@RequestBody CoursePageSearchDTO params);

    /**
     * 退课
     * @param dropOut 参数
     * @return 是否成功
     */
    @PostMapping("/student/drop")
    R<Boolean> dropOut(@RequestBody SignUpDTO dropOut);

    @PostMapping("/student/learn")
    R<List<CourseStudentLearnDTO>> studentLearn(@RequestBody SignUpDTO request);

    @PostMapping("/select/list")
    R<List<CourseListResultDTO>> listSelect(@RequestBody CourseListSearchDTO params);

    /**
     * 讲师是否关联课程
     * */
    @PostMapping("/isRelevance")
    R<List<CourseLecturerDetailDTO>> isRelevance(@RequestBody @Validated IdListRequest params);

    /**
     * 修改课程讲师信息
     * */
    @PostMapping("/updateCourseLecturer")
    R<Boolean> updateCourseLecturer(@RequestBody CourseLecturerDTO params);

    @PostMapping("inner/info")
    R<CourseDetailResponseDTO> getCourse(@RequestBody IdRequest request);

    @PostMapping("inner/getIdList")
    R<List<Long>> getIdList(@RequestBody CoursePageSearchDTO params);

    @PostMapping("inner/getCourseLesson")
    R<List<CourseLessonDTO>> getCourseLesson(@RequestBody IdRequest request);

    @PostMapping("/inner/list")
    R<List<CoursePageResultDTO>> listByIds(@RequestBody IdListRequest params);
}
