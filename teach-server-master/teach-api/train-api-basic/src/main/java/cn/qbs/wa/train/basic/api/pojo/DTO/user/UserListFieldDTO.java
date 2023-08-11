package cn.qbs.wa.train.basic.api.pojo.DTO.user;

import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/22 16:18
 */
@Data
public class UserListFieldDTO extends EncodeUser {


    @ApiModelProperty(value = "【真实姓名】")
    private String realName;
}
