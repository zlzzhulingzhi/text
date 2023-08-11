package cn.qbs.wa.teach.organization.pojo.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 个人信息参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:22
 */
@Data
public class PersonalInfoUpdateRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "主键标识")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;
}

