package cn.qbs.wa.teach.basic.pojo.user;

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
public class UserListFieldRequest extends EncodeUser {


    @ApiModelProperty(value = "【真实姓名】")
    private String realName;
}
