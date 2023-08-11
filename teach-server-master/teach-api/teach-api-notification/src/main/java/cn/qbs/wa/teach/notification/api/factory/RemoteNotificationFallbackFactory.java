package cn.qbs.wa.teach.notification.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.notification.api.RemoteNotificationService;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.*;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RemoteNotificationFallbackFactory implements FallbackFactory<RemoteNotificationService> {
    @Override
    public RemoteNotificationService create(Throwable cause) {
        return new RemoteNotificationService() {
            @Override
            public R<Boolean> setNotificationChannel(AutoNotificationDTO params, String source) {
                return null;
            }

            @Override
            public R<List<ListNotificationChannelDTO>> listNotificationChannel(BizNotificationDTO bizNotificationDTO) {
                return null;
            }

            @Override
            public R<Boolean> sendNotification(SendNotificationDTO params, String source) {
                return null;
            }

            @Override
            public R<Boolean> sendCommonNotification(SendNotificationDTO params, String source) {
                return null;
            }
        };
    }
}
