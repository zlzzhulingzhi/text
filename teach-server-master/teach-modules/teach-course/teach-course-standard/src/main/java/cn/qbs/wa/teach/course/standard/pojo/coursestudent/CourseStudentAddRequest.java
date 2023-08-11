package cn.qbs.wa.teach.course.standard.pojo.coursestudent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【课程学员】(CourseStudent)创建【课程学员】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseStudentAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @NotNull(message = "学员ID不能为空")
    @ApiModelProperty(value = "【学员ID】")
    private Long studentId;

    @NotNull(message = "报名途径不能为空")
    @ApiModelProperty(value = "【报名途径，ture-后台指派 false-购买途径】")
    private Boolean assign = Boolean.FALSE;

}

