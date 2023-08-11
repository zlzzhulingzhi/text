package cn.qbs.wa.teach.course.standard.pojo.tcourse;

import cn.qbs.wa.teach.course.standard.entity.TCourse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程-预报名学员(TCourseStudent)分页查询课程-预报名学员响应
 *
 * @author makejava
 * @since 2022-10-27 14:46:00
 */
@Data
public class TCoursePageResponse extends TCourse {

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "学员状态")
    private Integer status;

}

