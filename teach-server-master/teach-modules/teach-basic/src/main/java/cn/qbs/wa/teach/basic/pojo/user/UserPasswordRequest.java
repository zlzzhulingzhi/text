package cn.qbs.wa.teach.basic.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/6 14:38
 */
@Data
public class UserPasswordRequest {

    @ApiModelProperty("账号")
    String account;

    @ApiModelProperty("密码")
    String password;
}
