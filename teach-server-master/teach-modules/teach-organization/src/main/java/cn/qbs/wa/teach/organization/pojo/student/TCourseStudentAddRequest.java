package cn.qbs.wa.teach.organization.pojo.student;


import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 学员(Student)创建学员参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class TCourseStudentAddRequest{

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String phone;


}

