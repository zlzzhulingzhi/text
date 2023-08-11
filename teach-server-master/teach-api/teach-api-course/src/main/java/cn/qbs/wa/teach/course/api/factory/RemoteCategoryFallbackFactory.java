package cn.qbs.wa.teach.course.api.factory;

import cn.qbs.wa.teach.course.api.RemoteCategoryService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author yxj
 */
@Component
public class RemoteCategoryFallbackFactory implements FallbackFactory<RemoteCategoryService> {

    @Override
    public RemoteCategoryService create(Throwable cause) {
        return null;
    }
}
