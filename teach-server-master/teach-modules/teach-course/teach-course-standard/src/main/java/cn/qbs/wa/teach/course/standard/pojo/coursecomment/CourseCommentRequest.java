package cn.qbs.wa.teach.course.standard.pojo.coursecomment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【课程评价】(CourseComment)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseCommentRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【学员ID】")
    private Long userId;

}

