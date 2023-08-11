package cn.qbs.wa.teach.notification.api.pojo.DTO.notification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 业务主动发送通知请求参数
 *
 * @author makejava
 * @since 2022-05-26 10:26:12
 */
@Data
public class SendCommonNotificationDTO extends BizNotificationDTO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "渠道类型 ChannelEnum")
    private String channel;

    @ApiModelProperty(value = "参数")
    private List<String> params;


}

