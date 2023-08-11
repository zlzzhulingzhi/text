package cn.qbs.wa.teach.out.union.admin.api.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UniMemberDTO {

    @ApiModelProperty(value = "【会员ID】")
    private Long id;

    @ApiModelProperty(value = "【账号】")
    private String account;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "学历(字典值)")
    private String education;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "毕业院校")
    private String school;

    @ApiModelProperty(value = "出声年月")
    private LocalDate birthday;

    @ApiModelProperty(value = "民族(字典值)")
    private String nation;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "工作地址")
    private String workAddress;

    @ApiModelProperty(value = "性别 0-未知 1-男 2-女")
    private Integer sex;

    private Integer enabled;

    @ApiModelProperty(value = "户籍地址")
    private String registerAddress;

    @ApiModelProperty(value = "居住地")
    private String resideAddress;
}

