package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class StudentDTO {

    @ApiModelProperty(value = "【主键】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【组织机构】")
    private String orgName;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【账号】")
    private String account;

    @ApiModelProperty(value = "【密码】")
    private String password;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【身份号码】")
    private String idNumber;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;

    @ApiModelProperty(value = "【优惠券使用状态】")
    private Integer status;

    @ApiModelProperty(value = "【标签集合】")
    private List<String> groupNameList;

    @ApiModelProperty(value = "【标签名】")
    private String groupName;

    @ApiModelProperty(value = "【标签id】")
    private String groupId;

    @ApiModelProperty(value = "【标签集合】")
    private List<Long> groupIdList;

    @ApiModelProperty(value = "【学员身份 1-普通学员(默认) 2-内部员工")
    private Integer identity;

    @ApiModelProperty(value = "【部门名】")
    private String deptName;

    @ApiModelProperty(value = "【部门id】")
    private Long deptId;

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;

    @ApiModelProperty(value = "【父部门id】")
    private List<Long> deptPidList;

    @ApiModelProperty(value = "标签集合")
    private List<StudentGroupListDTO> groupList;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "学历(字典值)")
    private String education;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "毕业院校")
    private String school;

    @ApiModelProperty(value = "出声年月")
    private LocalDate birthday;

    @ApiModelProperty(value = "民族(字典值)")
    private String nation;

    @ApiModelProperty(value = "工作地址")
    private String workAddress;

    @ApiModelProperty(value = "民族")
    private String nationName;

    @ApiModelProperty(value = "户籍地址")
    private String registerAddress;

    @ApiModelProperty(value = "居住地")
    private String resideAddress;
}
