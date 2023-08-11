package cn.qbs.wa.union.auth.pojo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 第三方扫码登录对象
 *
 * @author yjx
 */
@Data
public class WechatBindingRequest {



    /**
     * 接口调用凭证
     */
    @NotBlank(message = "凭证不能为空")
    @ApiModelProperty(value = "凭证")
    private String accessToken;

    /**
     * 接口调用凭证
     */
    @NotBlank(message = "openId不能为空")
    @ApiModelProperty(value = "openId")
    private String openId;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    String code;

    /**
     * appid
     */
    @NotBlank(message = "appid不能为空")
    @ApiModelProperty(value = "微信应用appid")
    private String appid;

    @NotBlank(message = "应用编码不能为空")
    @ApiModelProperty(value = "应用编码 [admin 管理员 org 机构职工]",example = "admin")
    private String loginCode;
}
