package cn.qbs.wa.teach.course.standard.pojo.courseshare;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CourseShareRequest {

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "页面不能为空")
    @ApiModelProperty(value = "页面", example = "pages/home/index")
    private String page;

    @ApiModelProperty(value = "页面参数", example = "id=1&ep=2")
    private String scene;

}
