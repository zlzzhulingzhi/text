package cn.qbs.wa.teach.course.standard.pojo.dto.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【课程】(Course)查询参数
 *
 * @author yjx
 * @version 1.0
 */
@Data
public class ListCourseDTO {

    @ApiModelProperty(value = "【内外训标识 true-内训，false-外训】")
    private Boolean inner;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;
}

