package cn.qbs.wa.union.admin.pojo.unimember;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PhoneUpdateRequest {

    @NotBlank(message = "新手机号不能为空")
    @ApiModelProperty(value = "新手机号")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;
}
