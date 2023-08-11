package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

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
public class StudentPasswordDTO {
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;
}
