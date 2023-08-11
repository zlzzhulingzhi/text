package cn.qbs.wa.union.auth.pojo.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 第三方扫码登录对象
 *
 * @author yjx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniAppLoginRequest {

    @NotBlank(message = "openId不能为空")
    @ApiModelProperty(value = "openId")
    private String openId;

}
