package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.course.api.RemotePlatformCourseService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author yxj
 */
@Component
public class RemotePlatformCourseFallbackFactory implements FallbackFactory<RemotePlatformCourseService> {


    @Override
    public RemotePlatformCourseService create(Throwable cause) {
        return null;
    }
}
