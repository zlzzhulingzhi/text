package cn.qbs.wa.teach.basic.pojo.useroauth;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class OauthInfoResponse {
    
    @ApiModelProperty(value = "【主键】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String phone;

    @ApiModelProperty(value = "【账号】")
    @EncodeContent
    private String account;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【身份号码】")
    @EncodeContent
    private String idNumber;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【密码设置标识 true:未设置】")
    private boolean pwdMiss;

    @ApiModelProperty(value = "微信ID")
    private String uid;
}
