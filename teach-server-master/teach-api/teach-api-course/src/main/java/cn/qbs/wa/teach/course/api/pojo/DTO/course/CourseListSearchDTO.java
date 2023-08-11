package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【课程】(Course)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseListSearchDTO {

    private Long courseId;

    private List<Long> courseIdList;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【讲师】")
    private String lecturerName;
}

