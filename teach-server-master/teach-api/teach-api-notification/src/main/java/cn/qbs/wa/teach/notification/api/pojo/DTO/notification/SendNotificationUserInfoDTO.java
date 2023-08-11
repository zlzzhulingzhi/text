package cn.qbs.wa.teach.notification.api.pojo.DTO.notification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务主动发送通知请求参数
 *
 * @author makejava
 * @since 2022-05-26 10:26:12
 */
@Data
public class SendNotificationUserInfoDTO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "短信接收者电话(站内信时不传)")
    private String phone;

    @ApiModelProperty(value = "短信接收者姓名(站内信时不传)")
    private String name;

}

