package cn.qbs.wa.union.admin.pojo.systemuseroauth;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户第三方应用登录绑定表(SystemUserOauth)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
@Data
public class SystemUserOauthPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【应用ID】")
    private Long appId;

    @ApiModelProperty(value = "【授权第三方应用 如：weixin、weibo】")
    private String oauthApp;

    @ApiModelProperty(value = "【授权第三方应用的用户唯一ID】")
    private String uid;

}

