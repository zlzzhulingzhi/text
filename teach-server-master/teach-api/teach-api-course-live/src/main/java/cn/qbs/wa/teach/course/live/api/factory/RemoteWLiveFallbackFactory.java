package cn.qbs.wa.teach.course.live.api.factory;


import cn.qbs.wa.teach.course.live.api.RemoteWLiveService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Administrator
 */
public class RemoteWLiveFallbackFactory implements FallbackFactory<RemoteWLiveService> {


    @Override
    public RemoteWLiveService create(Throwable cause) {
        return null;
    }
}
