package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author ruoyi
 */
@Data
public class LoginRequest
{
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String account;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "请求id")
    private String requestId;





}
