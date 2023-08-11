package cn.qbs.wa.teach.course.api.pojo.DTO.courseLecture;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【课程讲师】(CourseLecturer)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseLecturerPageRequestDTO{

    @ApiModelProperty(value = "【讲师ID】")
    private Long lecturerId;


}

