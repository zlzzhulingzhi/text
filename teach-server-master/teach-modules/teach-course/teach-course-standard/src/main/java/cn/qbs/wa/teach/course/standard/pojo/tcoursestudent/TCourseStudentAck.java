package cn.qbs.wa.teach.course.standard.pojo.tcoursestudent;


import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.domain.BasePageRequest;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
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
public class TCourseStudentAck{

    @ApiModelProperty(value = "预报名课程学生信息")
    private TCourseStudent tCourseStudent;

    @ApiModelProperty(value = "学生信息")
    private StudentDTO studentDTO;

    @ApiModelProperty(value = "课程学生信息")
    private CourseStudent courseStudent;

}

