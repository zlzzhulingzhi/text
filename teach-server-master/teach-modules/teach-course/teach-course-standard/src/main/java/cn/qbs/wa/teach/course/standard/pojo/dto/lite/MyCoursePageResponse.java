package cn.qbs.wa.teach.course.standard.pojo.dto.lite;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MyCoursePageResponse extends PageCourseVO {

    @ApiModelProperty(value = "学员状态(0-带确认 1-已确认)")
    private Integer status;

}