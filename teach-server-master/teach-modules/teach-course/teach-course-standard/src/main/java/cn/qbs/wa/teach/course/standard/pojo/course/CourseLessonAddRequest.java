package cn.qbs.wa.teach.course.standard.pojo.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseLessonAddRequest {

    @NotBlank(message = "讲次名称不能为空")
    @ApiModelProperty(value = "【讲次名称】")
    private String lessonName;

    @ApiModelProperty(value = "【讲次简介】")
    private String introduction;

    @NotNull(message = "讲次排序不能为空")
    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @NotEmpty(message = "讲次内容不能为空")
    @ApiModelProperty(value = "【讲次内容列表】")
    private List<CourseComponentAddRequest> componentList;
}
