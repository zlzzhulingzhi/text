package cn.qbs.wa.teach.course.standard.pojo.coursecomponent;


import cn.qbs.wa.teach.course.standard.pojo.course.CourseComponentAddRequest;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲次内容】(CourseComponent)创建【课程讲次内容】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ComponentAddRequest extends CourseComponentAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "讲次ID不能为空")
    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @NotNull(message = "章节ID不能为空")
    @ApiModelProperty(value = "【章节ID】")
    private Long chapterId;
}

