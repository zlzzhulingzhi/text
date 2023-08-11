package cn.qbs.wa.union.auth.pojo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 第三方扫码登录对象
 *
 * @author yjx
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserLoginRequest extends MiniAppLoginRequest {

    @NotBlank(message = "应用编码不能为空")
    @ApiModelProperty(value = "应用编码 [admin 管理员 org 机构职工]", example = "admin")
    private String loginCode;

    public UserLoginRequest(String openId, String loginCode) {
        super(openId);
        this.loginCode = loginCode;
    }
}
