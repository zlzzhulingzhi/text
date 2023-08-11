package cn.qbs.wa.train.logistics.pojo.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@Data
public class PersonalPasswordUpdateRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "主键标识")
    private Long id;

    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String newPassword;

    @NotBlank(message = "确认新密码不能为空")
    @ApiModelProperty(value = "确认新密码")
    private String confirmPassword;
}