package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程相关资源ID信息
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseRelationDTO {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【章节ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;
}

