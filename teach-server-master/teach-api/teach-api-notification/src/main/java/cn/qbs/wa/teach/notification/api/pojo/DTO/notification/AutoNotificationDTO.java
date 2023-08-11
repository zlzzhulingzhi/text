package cn.qbs.wa.teach.notification.api.pojo.DTO.notification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 自动通知配置参数
 *
 * @author makejava
 * @since 2022-05-25 15:31:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AutoNotificationDTO extends BizNotificationDTO {

    @ApiModelProperty(value = "业务类型 1-培训项目 2-在线课堂")
    private Integer businessType;

    @ApiModelProperty(value = "业务ID")
    private Long businessId;

    @ApiModelProperty(value = "通知渠道")
    private List<String> channelCode;

}

