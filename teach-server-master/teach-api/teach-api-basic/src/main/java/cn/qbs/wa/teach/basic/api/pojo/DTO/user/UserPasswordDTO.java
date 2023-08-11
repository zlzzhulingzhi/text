package cn.qbs.wa.teach.basic.api.pojo.DTO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/6 14:42
 */
@Data
public class UserPasswordDTO {
    @ApiModelProperty("账号")
    String account;

    @ApiModelProperty("密码")
    String password;
}
