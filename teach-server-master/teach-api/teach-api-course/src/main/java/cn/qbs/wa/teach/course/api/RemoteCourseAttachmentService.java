package cn.qbs.wa.teach.course.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.factory.RemoteCourseAttachmentFallbackFactory;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseComponentUpdateResourceFileNameDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.ResourceFileDurationRecordDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteCourseAttachmentService", name = "teach-course", path = "course/attach", fallbackFactory = RemoteCourseAttachmentFallbackFactory.class)
public interface RemoteCourseAttachmentService {

    /**
     * 直播结束生成回放保存直播总时长
     */
    @ApiOperation(value = "直播结束生成回放保存直播总时长")
    @PostMapping("/resourceFileDurationRecording")
     R<Boolean> resourceFileDurationRecording(@RequestBody ResourceFileDurationRecordDTO params) ;

    /**
     * 课程讲次内容附近名称-修改
     */
    @ApiOperation(value = "课程讲次内容附近名称-修改")
    @PostMapping("/update/resourceFileName")
    R<Boolean> updateResourceFileName(@RequestBody @Validated CourseComponentUpdateResourceFileNameDTO params);
}
