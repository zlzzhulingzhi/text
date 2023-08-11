package cn.qbs.wa.union.auth.pojo.login;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户绑定微信关系表(UserOauth)创建用户绑定微信关系表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
@Data
public class UserOauthBindingRequest  {

    @ApiModelProperty(value = "【微信用户ID】")
    private String uid;

    @ApiModelProperty(value = "用户ID",hidden = true)
    private Long userId;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【性别】")
    private Integer sex;


    @NotBlank(message = "应用编码不能为空")
    @ApiModelProperty(value = "应用编码 [admin 管理员 org 机构职工]",example = "admin")
    private String loginCode;
}

