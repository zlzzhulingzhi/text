package cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【学员课程课次信息】(CourseStudentLesson)更新【学员课程课次信息】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-07 14:16:23
 */
@Data
public class CourseStudentLessonUpdateRequest {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【学员ID】")
    private Long userId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程章节ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【播放位置】")
    private Integer playbackPosition;

    @ApiModelProperty(value = "【播放进度，正整数】")
    private Integer playbackProgress;

    @ApiModelProperty(value = "【学习完成状态 0：未完成， 1：已完成】")
    private Integer learningCompletion;

}

