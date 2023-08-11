package cn.qbs.wa.teach.course.standard.pojo.tcoursestudent;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 课程-预报名学员(TCourseStudent)分页查询参数
 *
 * @author makejava
 * @since 2022-10-27 14:45:56
 */
@Data
public class TCourseStudentUpdateRequest {

    @ApiModelProperty(value = "学员状态(0-待确认 1-已确认)")
    private Integer status;

    @ApiModelProperty(value = "主键")
    private Long tCourseStudentId;

}

