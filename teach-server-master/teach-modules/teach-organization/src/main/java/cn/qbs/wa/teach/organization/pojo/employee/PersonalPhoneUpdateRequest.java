package cn.qbs.wa.teach.organization.pojo.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 个人信息参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:22
 */
@Data
public class PersonalPhoneUpdateRequest {
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "主键标识")
    private Long id;

    @NotBlank(message = "新手机号不能为空")
    @ApiModelProperty(value = "新手机号")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;

}

