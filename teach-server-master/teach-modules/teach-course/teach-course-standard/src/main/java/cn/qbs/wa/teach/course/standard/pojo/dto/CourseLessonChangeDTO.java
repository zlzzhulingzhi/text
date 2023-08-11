package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yjx
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseLessonChangeDTO extends CourseRelationDTO {

    @ApiModelProperty(value = "操作类型 add remove")
    private String action;
}
