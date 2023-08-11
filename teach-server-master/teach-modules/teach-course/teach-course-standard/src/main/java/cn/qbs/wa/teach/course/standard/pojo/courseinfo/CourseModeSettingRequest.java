package cn.qbs.wa.teach.course.standard.pojo.courseinfo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【课程信息】(CourseInfo)更新【课程信息】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseModeSettingRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "章节模式不能为空")
    @ApiModelProperty(value = "【内容模式 1-章节模式 2-节模式 3-无章节模式】")
    private Integer courseMode;

}

