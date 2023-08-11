package cn.qbs.wa.teach.course.standard.pojo.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseChapterAddRequest {

    @NotBlank(message = "章节名称不能为空")
    @ApiModelProperty(value = "【章节名称】")
    private String chapterName;

    @ApiModelProperty(value = "【章节简介】")
    private String introduction;

    @NotNull(message = "章节排序不能为空")
    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【讲次列表】")
    private List<CourseLessonAddRequest> lessonList;
}
