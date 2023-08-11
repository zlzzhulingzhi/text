package cn.qbs.wa.union.auth.pojo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 第三方扫码登录对象
 *
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserBindingRequest extends MiniAppBindingRequest {

    @NotBlank(message = "应用编码不能为空")
    @ApiModelProperty(value = "应用编码 [admin 管理员 org 机构职工]", example = "admin")
    private String loginCode;

    @ApiModelProperty(value = "重新绑定标识", example = "false")
    private Boolean reBind;

    @ApiModelProperty(value = "手机号来源", notes = "manual:手动输入、weixin:微信获取", example = "manual")
    private String mode;

    @ApiModelProperty(value = "验证码", notes = "手动输入手机号需要传值")
    private String code;
}
