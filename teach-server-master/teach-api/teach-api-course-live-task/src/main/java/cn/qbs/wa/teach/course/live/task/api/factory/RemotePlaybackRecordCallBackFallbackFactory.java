package cn.qbs.wa.teach.course.live.task.api.factory;

import cn.qbs.wa.teach.course.live.task.api.RemotePlayBackRecordCallBackService;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemotePlaybackRecordCallBackFallbackFactory implements FallbackFactory<RemotePlayBackRecordCallBackService> {


    @Override
    public RemotePlayBackRecordCallBackService create(Throwable cause) {
        return null;
    }
}
