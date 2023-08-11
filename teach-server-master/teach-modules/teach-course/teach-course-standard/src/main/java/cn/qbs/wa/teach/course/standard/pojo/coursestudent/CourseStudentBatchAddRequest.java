package cn.qbs.wa.teach.course.standard.pojo.coursestudent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 【课程学员】(CourseStudent)创建【课程学员】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseStudentBatchAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @Valid
    @NotEmpty(message = "用户信息为空")
    private List<SignUpUser> signUpInfos;

    @Data
    public static class SignUpUser {

        @NotNull(message = "用户ID不能为空")
        @ApiModelProperty(value = "【系统用户ID】")
        private Long userId;

        @NotNull(message = "学员ID不能为空")
        @ApiModelProperty(value = "【学员ID】")
        private Long studentId;

        @NotNull(message = "添加途径不能为空")
        @ApiModelProperty(value = "【后台手动添加指派报名】")
        private boolean assign = false;
    }

}

