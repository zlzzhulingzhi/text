package cn.qbs.wa.train.basic.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-11-12 09:25:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeixinUser extends Model {

    private static final long serialVersionUID = 204747599236042496L;


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "对应syst_user表的id")
    private Long userId;

    @ApiModelProperty(value = "普通用户的标识，对当前开发者帐号唯一")
    private String openid;

    @ApiModelProperty(value = "用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。")
    private String unionid;

    @ApiModelProperty(value = "普通用户昵称")
    private String nickname;

    @ApiModelProperty(value = "普通用户性别，1为男性，2为女性")
    private String sex;

    @ApiModelProperty(value = "普通用户个人资料填写的省份")
    private String province;

    @ApiModelProperty(value = "普通用户个人资料填写的城市")
    private String city;

    @ApiModelProperty(value = "国家，如中国为CN")
    private String country;

    @ApiModelProperty(value = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空")
    private String headimgurl;

    @ApiModelProperty(value = "接口调用凭证")
    private String accessToken;

    @ApiModelProperty(value = "获取凭证时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime accessTokenDate;

    @ApiModelProperty(value = "用户刷新access_token凭证")
    private String refreshToken;

    @ApiModelProperty(value = "获取refresh_token时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime refreshTokenDate;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateDate;

}
