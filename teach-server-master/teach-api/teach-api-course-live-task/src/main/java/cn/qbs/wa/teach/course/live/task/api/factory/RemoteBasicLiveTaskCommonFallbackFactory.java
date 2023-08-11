package cn.qbs.wa.teach.course.live.task.api.factory;

import cn.qbs.wa.teach.course.live.task.api.RemoteBasicLiveTaskCommonService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteBasicLiveTaskCommonFallbackFactory implements FallbackFactory<RemoteBasicLiveTaskCommonService> {


    @Override
    public RemoteBasicLiveTaskCommonService create(Throwable cause) {
        return null;
    }
}
