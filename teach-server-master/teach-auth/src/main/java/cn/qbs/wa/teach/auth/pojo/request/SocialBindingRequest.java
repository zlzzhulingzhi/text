package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 第三方扫码登录对象
 *
 * @author yjx
 */
@Data
public class SocialBindingRequest {

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

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
    @ApiModelProperty(value = "appid")
    private String appid;
}
