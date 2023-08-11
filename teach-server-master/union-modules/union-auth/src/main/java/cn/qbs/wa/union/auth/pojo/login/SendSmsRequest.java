package cn.qbs.wa.union.auth.pojo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 9:01
 */
@Data
public class SendSmsRequest {

    @ApiModelProperty(value = "操作", example = "login")
    @NotBlank(message = "短信操作不能为空")
    String action;

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    String account;

    @NotBlank(message = "应用编码不能为空")
    @ApiModelProperty(value = "应用编码 [admin 管理员 org 机构职工]",example = "admin")
    private String loginCode;
}
