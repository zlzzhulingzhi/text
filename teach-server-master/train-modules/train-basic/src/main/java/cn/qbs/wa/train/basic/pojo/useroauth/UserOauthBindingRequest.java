package cn.qbs.wa.train.basic.pojo.useroauth;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户绑定微信关系表(UserOauth)创建用户绑定微信关系表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserOauthBindingRequest extends UserAccountRequest {

    @ApiModelProperty(value = "【微信用户ID】")
    private String uid;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【性别】")
    private Integer sex;
}

