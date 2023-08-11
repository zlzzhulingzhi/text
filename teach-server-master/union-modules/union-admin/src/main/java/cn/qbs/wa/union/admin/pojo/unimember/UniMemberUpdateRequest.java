package cn.qbs.wa.union.admin.pojo.unimember;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 统一会员用户表(UniMember)统一会员用户表详情
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
@Data
public class UniMemberUpdateRequest {

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "学历(字典值)")
    private String education;

    @ApiModelProperty(value = "性别 0-未知 1-男 2-女")
    private Integer sex;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "出声年月")
    private LocalDate birthday;

    @ApiModelProperty(value = "民族(字典值)")
    private String nation;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "毕业院校")
    private String school;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "工作地址")
    private String workAddress;

    @ApiModelProperty(value = "户籍地址")
    private String registerAddress;

    @ApiModelProperty(value = "居住地")
    private String resideAddress;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;
}

