package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseCenterFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageByChildResultDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageResultDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageSearchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseCenterService", name = "teach-course", path = "course/center", fallbackFactory = RemoteCourseCenterFallbackFactory.class)
public interface RemoteCourseCenterService {

    /**
     * 分页查询【课程】
     */
    @ApiOperation(value = "首页-课程分页查询")
    @PostMapping("/index/page")
    R<PageResultComDTO<CoursePageResultDTO>> pageCourseByIndex(@RequestBody CoursePageSearchDTO params);

    @PostMapping("/index/pageByChild")
    R<PageResultComDTO<CoursePageByChildResultDTO>> pageByChild(@RequestBody CoursePageSearchDTO params);
}
