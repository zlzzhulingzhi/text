package cn.qbs.wa.train.logistics.api.pojo.DTO.employee;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 职工(Employee)创建职工参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:21
 */
@Data
public class EmployeeAddRequestDTO {

    @ApiModelProperty(value = "【员工ID】")
    private Long id;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    @NotBlank(message = "手机号码为空")
    private String phone;

    @ApiModelProperty(value = "【账号】")
    @NotBlank(message = "手机号码为空")
    private String account;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【密码 若加盐就是加密后的密文】")
    private String password;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "0表示账号不可用  1表示账号可用")
    private Integer enabled;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "角色id数组")
    private List<Long> roleIdList;

    @ApiModelProperty(value = "【部门ID数组】")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "是否创建学员账号")
    private boolean createStudent;
}

