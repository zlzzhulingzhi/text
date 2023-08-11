package cn.qbs.wa.train.logistics.pojo.student;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class LoginInfoResponse {

    @ApiModelProperty(value = "【主键】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【职员ID】")
    private Long employeeId;

    @ApiModelProperty(value = "【账号】")
    @EncodeContent
    private String account;

    @ApiModelProperty(value = "【密码】")
    private String password;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;

}
