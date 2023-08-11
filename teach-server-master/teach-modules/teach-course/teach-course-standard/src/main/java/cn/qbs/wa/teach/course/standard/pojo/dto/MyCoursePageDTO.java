package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的课程分页对象
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyCoursePageDTO extends CoursePageViewDTO {

    @ApiModelProperty(value = "【学习完成率】")
    private Integer learningRate;

    @ApiModelProperty(value = "【学习完成状态 0：未完成， 1：已完成】")
    private Integer learningCompletion;

    @ApiModelProperty(value = "课程最后学习记录")
    private CourseLessonSimpleDTO lesson;

    @ApiModelProperty(value = "直播状态 , 0未直播 1直播中 2直播结束")
    private Integer liveStatus;

}
