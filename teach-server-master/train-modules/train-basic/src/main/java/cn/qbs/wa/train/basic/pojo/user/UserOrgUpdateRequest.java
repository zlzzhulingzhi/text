package cn.qbs.wa.train.basic.pojo.user;

import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 14:09
 */
@Data
public class UserOrgUpdateRequest extends EncodeUser {

    @ApiModelProperty(value = "【id】")
    @NotNull(message = "id不能为空")
    private Long  id;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【密码 若加盐就是加密后的密文】")
    private String password;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;


}
