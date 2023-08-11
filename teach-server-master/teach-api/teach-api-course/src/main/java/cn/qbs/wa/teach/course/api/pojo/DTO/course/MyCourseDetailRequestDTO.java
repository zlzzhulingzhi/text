package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的课程分页对象
 * @author yjx
 */
@Data
public class MyCourseDetailRequestDTO {
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;
}
