package cn.qbs.wa.teach.organization.api.pojo.DTO.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateBindUserDTO {

    @ApiModelProperty(value = "【旧用户ID】")
    private Long oldUserId;

    @ApiModelProperty(value = "标识")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【账号】")
    private String account;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【密码 若加盐就是加密后的密文】")
    private String password;

}

