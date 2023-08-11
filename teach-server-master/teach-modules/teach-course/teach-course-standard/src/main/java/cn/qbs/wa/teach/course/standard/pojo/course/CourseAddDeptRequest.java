package cn.qbs.wa.teach.course.standard.pojo.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 【课程】添加所属部门参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseAddDeptRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程id不能为空")
    @ApiModelProperty(value = "【课程id】")
    private List<Long> courseId;

    @ApiModelProperty(value = "【部门id】")
    @NotNull(message = "部门id不能为空")
    private Long deptId;

}

