package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 课程学习记录
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class LiveCourseStudyingDTO {

    @ApiModelProperty(value = "【内外训标识 true-内训，false-外训】")
    private Boolean inner;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "用户id不为空")
    @ApiModelProperty(value = "【用户id】")
    private Long userId;

    @NotNull(message = "学习时长不为空")
    @ApiModelProperty(value = "【学习时长】")
    private Long learningDuration;

    @NotNull(message = "直播id不为空")
    @ApiModelProperty(value = "【课程id】")
    private Long componentId;

    @ApiModelProperty(value = "【播放设备 web】")
    private String playbackDevice;
}

