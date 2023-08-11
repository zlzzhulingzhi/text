package cn.qbs.wa.union.admin.pojo.systemuseroauth;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户第三方应用登录绑定表(SystemUserOauth)创建用户第三方应用登录绑定表参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
@Data
public class SystemUserOauthAddRequest {

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【应用ID】")
    private Long appId;

    @ApiModelProperty(value = "【授权第三方应用 如：weixin、weibo】")
    private String oauthApp;

    @ApiModelProperty(value = "【授权第三方应用的用户唯一ID】")
    private String uid;

}

