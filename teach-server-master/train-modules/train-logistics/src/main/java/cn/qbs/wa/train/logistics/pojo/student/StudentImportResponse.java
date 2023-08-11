package cn.qbs.wa.train.logistics.pojo.student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentImportResponse {

    @ApiModelProperty(value = "【账号】")
    private String account;

    @ApiModelProperty(value = "【手机号】")
    private String phone;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【备注信息】")
    private boolean invalid;

    @ApiModelProperty(value = "【备注信息】")
    private String remark;
}

