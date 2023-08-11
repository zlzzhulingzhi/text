package cn.qbs.wa.union.admin.pojo.systememployeeuser;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 平台系统子用户表(SystemUser)创建平台系统子用户表参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
@Data
public class InnerEmployeeUserAddRequest {



    @ApiModelProperty(value = "【账号】")
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "【手机号】")
    private String phone;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【密码 若加盐就是加密后的密文】")
    private String password;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "0表示账号不可用  1表示账号可用")
    private Integer enabled;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "应用编码")
    @NotBlank(message = "应用编码不能为空")
    private String appCode;





}

