package cn.qbs.wa.teach.notification.api.pojo.DTO.notification;

import cn.qbs.wa.teach.notification.api.enums.NotificationBusinessEnum;
import cn.qbs.wa.teach.notification.api.enums.RepeatCheckEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 业务主动发送通知请求参数
 *
 * @author makejava
 * @since 2022-05-26 10:26:12
 */
@Data
public class SendNotificationDTO {

    @ApiModelProperty(value = "业务类型")
    private NotificationBusinessEnum businessType;

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "通知模板变量对应的实际值。支持多个参数，示例：{'taskName':'XX培训任务','url':'www.baidu.com'}。")
    private Map<String,String> templateKey;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "需要发送的用户")
    private List<SendNotificationUserInfoDTO> userInfo;

    @ApiModelProperty(value = "通知重复检查")
    private RepeatCheckEnum repeatCheckEnum = RepeatCheckEnum.CLOSE;

}

