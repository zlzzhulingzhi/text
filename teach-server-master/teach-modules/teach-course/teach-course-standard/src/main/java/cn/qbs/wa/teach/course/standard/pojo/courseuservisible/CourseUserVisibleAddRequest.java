package cn.qbs.wa.teach.course.standard.pojo.courseuservisible;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程可见用户】(CourseUserVisible)创建【课程可见用户】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseUserVisibleAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【职工ID】")
    private Long employeeId;

}

