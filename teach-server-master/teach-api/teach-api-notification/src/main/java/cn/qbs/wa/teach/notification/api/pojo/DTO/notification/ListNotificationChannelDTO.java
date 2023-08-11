package cn.qbs.wa.teach.notification.api.pojo.DTO.notification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通知(Notification)通知详情
 *
 * @author makejava
 * @since 2022-05-16 15:31:13
 */
@Data
public class ListNotificationChannelDTO {

    @ApiModelProperty(value = "通知ID")
    private Long id;

    @ApiModelProperty(value = "通知渠道")
    private String channelCode;

}

