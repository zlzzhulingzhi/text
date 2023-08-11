package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可见用户
 * @Author zcm
 * @Date 2021-12-23 10:37
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisibleUser {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "职工ID")
    private Long employeeId;

    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号】")
    private String phone;

}
