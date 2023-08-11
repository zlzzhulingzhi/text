package cn.qbs.wa.teach.course.api.pojo.DTO.student;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【课程学员】(CourseStudent)创建【课程学员】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class SignUpDTO {

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;
    
    @ApiModelProperty(value = "【课程ID数组】")
    private List<Long> courseIdList;

    private List<Long> userIdList;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【学员ID】")
    private Long studentId;
}

