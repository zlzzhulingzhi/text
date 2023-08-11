package cn.qbs.wa.train.basic.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/5 9:19
 */
@Data
public class UserPasswordAdminRequest {

    @ApiModelProperty("用户id")
    Long userId;
}
