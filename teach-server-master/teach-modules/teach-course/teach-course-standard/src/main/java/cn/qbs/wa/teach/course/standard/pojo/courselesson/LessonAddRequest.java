package cn.qbs.wa.teach.course.standard.pojo.courselesson;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲次】(CourseLesson)创建【课程讲次】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class LessonAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程章节ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【讲次名称】")
    private String lessonName;

    @ApiModelProperty(value = "【讲次简介】")
    private String introduction;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}

