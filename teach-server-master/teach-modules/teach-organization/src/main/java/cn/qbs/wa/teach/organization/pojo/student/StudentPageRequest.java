package cn.qbs.wa.teach.organization.pojo.student;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学员(Student)分页查询参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class StudentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
    private Integer enabled;

    @ApiModelProperty(value = "【标签名】")
    private String groupName;

    @ApiModelProperty(value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(value = "【标签id】")
    private Long groupId;

    @ApiModelProperty(value = "【部门名】")
    private String deptName;

    @ApiModelProperty(value = "【部门id】")
    private Long deptId;

    @ApiModelProperty(value = "【部门id集合】")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "【标签id集合】")
    private List<Long> groupIdList;

    @ApiModelProperty(value = "【学生id集合】")
    private List<Long> studentIdList;

    @ApiModelProperty(value = "【学员身份 1-普通学员(默认) 2-内部员工")
    private Integer identity;

    @ApiModelProperty(value = "学员排序属性【realName/createTime】")
    private String studentSortField;

    @ApiModelProperty(value = "学员排序正序/倒序【asc/desc】")
    private String studentSortOrder;

    @ApiModelProperty(value = "注册开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime regStartTime;

    @ApiModelProperty(value = "注册结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime regEndTime;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "是否根据课程查询 是-1")
    private Integer byCourseId=0;
}

