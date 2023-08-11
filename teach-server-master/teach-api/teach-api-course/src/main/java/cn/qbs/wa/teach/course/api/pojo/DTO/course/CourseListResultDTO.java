package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【课程】(Course)分页查询【课程】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseListResultDTO {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【讲师】")
    private String lecturerNames;

    @ApiModelProperty(value = "【课程类型 record-录播 live-直播 mix-综合】")
    private String courseType;

}

