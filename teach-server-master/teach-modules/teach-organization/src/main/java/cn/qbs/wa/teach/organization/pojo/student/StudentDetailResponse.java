package cn.qbs.wa.teach.organization.pojo.student;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 学员(Student)学员详情
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class StudentDetailResponse {

    @ApiModelProperty(value = "【主键】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String phone;

    @ApiModelProperty(value = "【账号】")
    @EncodeContent
    private String account;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【身份号码】")
    @EncodeContent
    private String idNumber;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;

    @ApiModelProperty(value = "【密码设置标识 true:未设置】")
    private boolean pwdMiss;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;

    @ApiModelProperty(value = "微信ID")
    private String uid;

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

    @ApiModelProperty(value = "【父部门id】")
    private List<Long> deptPidList;

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

    @ApiModelProperty(value = "民族")
    private String nationName;

    @ApiModelProperty(value = "工作地址")
    private String workAddress;

    @ApiModelProperty(value = "户籍地址")
    private String registerAddress;

    @ApiModelProperty(value = "居住地")
    private String resideAddress;

}

