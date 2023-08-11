package cn.qbs.wa.union.auth.pojo.member;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberInfo {

    @ApiModelProperty("用户ID")
    private Long memberId;

    @EncodeContent
    @ApiModelProperty("手机号")
    private String account;

    @EncodeContent
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty("头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;
}
