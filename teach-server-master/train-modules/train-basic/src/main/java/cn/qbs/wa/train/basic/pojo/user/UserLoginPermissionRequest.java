package cn.qbs.wa.train.basic.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 14:26
 */
@Data
public class UserLoginPermissionRequest {

    @ApiModelProperty(value = "应用类别Id",example = "2平台管理后台 3机构管理后台")
    Long applicationTypeId;
}
