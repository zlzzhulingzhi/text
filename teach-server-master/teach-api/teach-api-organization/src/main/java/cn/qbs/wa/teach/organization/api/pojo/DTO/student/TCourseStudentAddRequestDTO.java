package cn.qbs.wa.teach.organization.api.pojo.DTO.student;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学员(Student)创建学员参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class TCourseStudentAddRequestDTO {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String phone;


}

