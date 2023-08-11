package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CourseStudentLearnDTO {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程ID数组】")
    private List<Long> courseIdList;

    @ApiModelProperty(value = "【系统用户ID数组】")
    private List<Long> userIdList;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【学员ID】")
    private Long studentId;

    @ApiModelProperty(value = "【学习完成率】")
    private Integer learningRate;
}
