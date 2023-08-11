package cn.qbs.wa.teach.course.standard.pojo.coursestudentstudyrecord;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程学习记录】(CourseStudentStudyRecord)更新【课程学习记录】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseStudentStudyRecordUpdateRequest {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程章节ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【学员ID】")
    private Long userId;

    @ApiModelProperty(value = "【播放设备】")
    private String playbackDevice;

    @ApiModelProperty(value = "【播放位置】")
    private Integer playbackPosition;

    @ApiModelProperty(value = "【播放进度】")
    private Integer playbackProgress;

    @ApiModelProperty(value = "【学习时长】")
    private Long learningDuration;

}

