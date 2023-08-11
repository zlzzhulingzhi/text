package cn.qbs.wa.teach.organization.pojo.student;

import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

/**
 * 学员(Student)创建学员参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentImportRequest extends EncodeUser {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【邮箱】")
    private String email;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "学生标签")
    private String groupName;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "数据校验结果")
    private Boolean passed;

    @ApiModelProperty(value = "错误原因")
    private Set<String> errorReasons;

}

