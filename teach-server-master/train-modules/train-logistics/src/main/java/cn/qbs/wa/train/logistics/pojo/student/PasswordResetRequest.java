package cn.qbs.wa.train.logistics.pojo.student;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty("账号")
    String account;

    @ApiModelProperty("密码")
    String password;

    @ApiModelProperty(value = "验证码")
    String code;
}