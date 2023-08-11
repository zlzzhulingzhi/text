package cn.qbs.wa.train.basic.api.pojo.DTO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/7 15:34
 */
@Data
public class UserPasswordAdminDTO {

    @ApiModelProperty("用户id")
    Long userId;
}
