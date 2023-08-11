package cn.qbs.wa.teach.course.standard.pojo.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseContentAddRequest {

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotEmpty(message = "章节列表不能为空")
    @ApiModelProperty(value = "【章节列表】")
    private List<CourseChapterAddRequest> chapterList;

}
