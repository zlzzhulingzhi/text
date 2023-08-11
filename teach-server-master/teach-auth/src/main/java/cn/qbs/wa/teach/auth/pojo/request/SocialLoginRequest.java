package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 第三方扫码登录对象
 *
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialLoginRequest extends OAuthCodeRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "unionId", hidden = true)
    private String unionId;

}
