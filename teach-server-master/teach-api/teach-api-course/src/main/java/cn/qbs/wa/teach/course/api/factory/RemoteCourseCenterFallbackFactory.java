package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.course.api.RemoteCourseCenterService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author yxj
 */
@Component
public class RemoteCourseCenterFallbackFactory implements FallbackFactory<RemoteCourseCenterService> {


    @Override
    public RemoteCourseCenterService create(Throwable cause) {
        return null;
    }
}
