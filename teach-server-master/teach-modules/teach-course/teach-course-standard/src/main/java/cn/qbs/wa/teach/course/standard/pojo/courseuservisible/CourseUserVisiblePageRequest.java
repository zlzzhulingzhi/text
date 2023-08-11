package cn.qbs.wa.teach.course.standard.pojo.courseuservisible;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程可见用户】(CourseUserVisible)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseUserVisiblePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【职工ID】")
    private Long employeeId;

}

