package cn.qbs.wa.teach.notification.api;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.notification.api.factory.RemoteNotificationFallbackFactory;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author yjx
 */
@FeignClient(contextId = "remoteNotificationService", name = "teach-notification", path = "notification/notification", fallbackFactory = RemoteNotificationFallbackFactory.class)
public interface RemoteNotificationService {

    /**
     * 课程报名或培训项目需要先设置通知渠道才能发送通知
     * */
    @ApiOperation(value = "设置通知渠道")
    @PostMapping("/setNotificationChannel")
    R<Boolean> setNotificationChannel(@RequestBody AutoNotificationDTO params, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @ApiOperation(value = "获取通知渠道列表")
    @PostMapping("/listNotificationChannel")
    R<List<ListNotificationChannelDTO>> listNotificationChannel(@RequestBody BizNotificationDTO bizNotificationDTO);

    @ApiOperation(value = "发送通知")
    @PostMapping("/sendNotification")
    R<Boolean> sendNotification(@RequestBody SendNotificationDTO params, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);



    /**
     * 无需设置通知渠道的业务直接发送站内信通知
     * */
    @ApiOperation(value = "常规发送通知")
    @PostMapping("/common/sendNotification")
    R<Boolean> sendCommonNotification(@RequestBody SendNotificationDTO params, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
