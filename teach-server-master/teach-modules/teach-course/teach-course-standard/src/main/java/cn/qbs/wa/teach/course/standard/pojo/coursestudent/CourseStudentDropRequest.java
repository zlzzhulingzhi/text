package cn.qbs.wa.teach.course.standard.pojo.coursestudent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【课程学员】(CourseStudent)创建【课程学员】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseStudentDropRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【学员ID】")
    private Long studentId;

}

