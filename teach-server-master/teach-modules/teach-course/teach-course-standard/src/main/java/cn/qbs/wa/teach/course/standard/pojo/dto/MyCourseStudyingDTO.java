package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 课程学习记录
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyCourseStudyingDTO extends CourseRelationDTO {

    @ApiModelProperty(value = "【内外训标识 true-内训，false-外训】")
    private Boolean inner;

    @NotNull(message = "播放进度不为空")
    @ApiModelProperty(value = "【播放位置】")
    private Integer playbackPosition;

    @NotNull(message = "学习时长不为空")
    @ApiModelProperty(value = "【学习时长】")
    private Long learningDuration;

    @ApiModelProperty(value = "【播放设备 web】")
    private String playbackDevice;
}

