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
public class OAuthCodeRequest {

    /**
     * 授权码
     */
    @NotBlank(message = "授权码不能为空")
    @ApiModelProperty(value = "授权码")
    private String code;

    /**
     * appid
     */
    @NotBlank(message = "appid不能为空")
    @ApiModelProperty(value = "appid")
    private String appid;

}
