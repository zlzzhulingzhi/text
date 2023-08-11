package cn.qbs.wa.teach.organization.pojo.appoauth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class AppOAuthUserRequest {

    private Long orgId;

    @ApiModelProperty(value = "【账号类别 employee-员工 student-学员】")
    private String accountType;

    @ApiModelProperty(value = "【第三方平台 如：weixin、weibo】")
    private String platform;

    @ApiModelProperty(value = "【第三方应用的用户唯一ID】")
    private String uid;
}
