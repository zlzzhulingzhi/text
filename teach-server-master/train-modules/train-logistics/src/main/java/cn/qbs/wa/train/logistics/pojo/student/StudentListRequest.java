package cn.qbs.wa.train.logistics.pojo.student;

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
public class StudentListRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【用户ID数组】")
    private List<Long> userIds;

    @ApiModelProperty(value = "【学员ID数组】")
    private List<Long> idList;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;

    @ApiModelProperty(value = "【学员所属部门id集合】")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "【学员所属标签id集合】")
    private List<Long> groupIdList;
}

