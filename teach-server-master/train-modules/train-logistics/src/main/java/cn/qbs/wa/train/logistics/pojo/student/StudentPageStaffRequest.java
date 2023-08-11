package cn.qbs.wa.train.logistics.pojo.student;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 学员(Student)分页查询参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class StudentPageStaffRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "内部学员查询标识-1")
    private Integer inner;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门数组id")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "角色数组id")
    private List<Long> roleIdList;

    @ApiModelProperty(value = "忽略的学员id")
    private List<Long> ignoreIdList;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "【标签名】")
    private String groupName;

    @ApiModelProperty(value = "【标签id】")
    private Long groupId;

    @ApiModelProperty(value = "【部门名】")
    private String deptName;

    @ApiModelProperty(value = "【学员身份 1-普通学员(默认) 2-内部员工")
    private Integer identity;
}

