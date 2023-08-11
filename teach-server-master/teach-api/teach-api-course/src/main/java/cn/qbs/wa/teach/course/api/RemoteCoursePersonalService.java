package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCoursePersonalFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseDetailResponseDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseLessonSimpleResponseDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.LiveCourseStudyingDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.MyCourseDetailRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCoursePersonalService", name = "teach-course", path = "course/personal", fallbackFactory = RemoteCoursePersonalFallbackFactory.class)
public interface RemoteCoursePersonalService {

    /**
     * 分页查询【我的课程】
     */
    @ApiOperation(value = "查询课程详情")
    @PostMapping("/detail")
    //@RequiresPermissions("course:details")
    R<List<CourseLessonSimpleResponseDTO>> detail(@RequestBody List<MyCourseDetailRequestDTO> params);

    /**
     * 直播学习情况收集
     */
    @ApiOperation(value = "直播课程学习情况收集")
    @PostMapping("/liveRecording")
     R<Boolean> liveRecording(@RequestBody List<LiveCourseStudyingDTO> params);

    /**
     * 查询课程详情
     */
    @ApiOperation(value = "查询课程详情")
    @PostMapping("/info")
    R<List<CourseDetailResponseDTO>> info(@RequestBody List<MyCourseDetailRequestDTO> params);
}
