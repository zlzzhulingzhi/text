package cn.qbs.wa.train.basic.api.pojo.DTO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 15:29
 */
@Data
public class UserOrgUpdateDTO {

    @ApiModelProperty(value = "【id】")
    @NotNull(message = "id不能为空")
    private Long  id;

    @ApiModelProperty(value = "【账号】")
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "【手机号】")
    private String phone;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【密码 若加盐就是加密后的密文】")
    private String password;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;
}
