package cn.qbs.wa.teach.auth.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 用户注册对象
 *
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentRegisterRequest extends RegisterRequest {

    @ApiModelProperty(value = "【手机号】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;
}
