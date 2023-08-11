package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/6 14:44
 */
@Data
public class UserPasswordRequest {


    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    String account;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    String code;


    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    String password;
}
