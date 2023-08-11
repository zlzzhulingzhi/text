package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class QualificationCourseRequest {

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "课程名称(快照)")
    private String courseName;

    @ApiModelProperty(value = "培训对象")
    private String trainObject;

    @ApiModelProperty(value = "培训内容及目标")
    private String trainContent;

    @ApiModelProperty(value = "讲师ID")
    private Long lecturerId;

    @ApiModelProperty(value = "主讲老师")
    private String lecturerName;
}
