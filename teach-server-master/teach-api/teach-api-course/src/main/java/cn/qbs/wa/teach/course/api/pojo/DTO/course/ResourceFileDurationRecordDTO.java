package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 直播总时长
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class ResourceFileDurationRecordDTO {
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【用户id】")
    private Long userId;

    @NotNull(message = "学习时长不为空")
    @ApiModelProperty(value = "【学习时长】")
    private Long resourceFileDuration;

    @NotNull(message = "组件ID不为空")
    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;
}

