package cn.qbs.wa.teach.notification.api.pojo.DTO.notification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务通知配置参数
 *
 * @author makejava
 * @since 2022-05-25 15:31:12
 */
@Data
public class BizNotificationCourseDTO {

    @ApiModelProperty(value = "业务名称")
    private String courseName;

}

