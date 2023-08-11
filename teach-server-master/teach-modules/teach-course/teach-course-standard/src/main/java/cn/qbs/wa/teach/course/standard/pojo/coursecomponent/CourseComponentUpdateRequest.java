package cn.qbs.wa.teach.course.standard.pojo.coursecomponent;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲次内容】(CourseComponent)更新【课程讲次内容】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseComponentUpdateRequest extends ComponentAddRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "【主键标识】")
    private Long id;

}

