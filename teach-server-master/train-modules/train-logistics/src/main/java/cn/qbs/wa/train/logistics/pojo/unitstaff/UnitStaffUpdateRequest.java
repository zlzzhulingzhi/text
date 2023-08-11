package cn.qbs.wa.train.logistics.pojo.unitstaff;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单位职员表(UnitStaff)更新单位职员表参数
 *
 * @author makejava
 * @since 2022-09-29 09:04:03
 */
@Data
public class UnitStaffUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "学历(字典值)")
    private String education;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "性别 0-未知 1-男 2-女")
    private Integer sex;

    @ApiModelProperty(value = "小程序用户ID")
    private String openId;

    @ApiModelProperty(value = "用户状态 0-不可用  1-可用")
    private String enabled;

}

