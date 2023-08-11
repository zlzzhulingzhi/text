package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
