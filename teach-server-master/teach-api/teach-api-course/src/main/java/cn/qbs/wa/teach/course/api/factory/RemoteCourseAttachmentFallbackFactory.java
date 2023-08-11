package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.api.RemoteCourseAttachmentService;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseComponentUpdateResourceFileNameDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.ResourceFileDurationRecordDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

public class RemoteCourseAttachmentFallbackFactory implements FallbackFactory<RemoteCourseAttachmentService> {
    @Override
    public RemoteCourseAttachmentService create(Throwable cause) {
        return new RemoteCourseAttachmentService() {
            @Override
            public R<Boolean> resourceFileDurationRecording(ResourceFileDurationRecordDTO params) {
                return null;
            }

            @Override
            public R<Boolean> updateResourceFileName(CourseComponentUpdateResourceFileNameDTO params) {
                return null;
            }
        };
    }
}

