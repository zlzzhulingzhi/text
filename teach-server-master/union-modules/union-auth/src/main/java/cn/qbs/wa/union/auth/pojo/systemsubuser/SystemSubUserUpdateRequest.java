package cn.qbs.wa.union.auth.pojo.systemsubuser;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 子用户表(SystemSubUser)更新子用户表参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
@Data
public class SystemSubUserUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "账号模式 1 共享模式 2独立模式")
    private Integer mode;

    @ApiModelProperty(value = "【应用id】")
    private Long appId;

    @ApiModelProperty(value = "【用户id 对应uni_user】")
    private Long userId;

    @ApiModelProperty(value = "【账号】")
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

}

