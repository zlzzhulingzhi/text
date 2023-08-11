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
public class CourseContentSortRequest {

    @NotEmpty(message = "调整层级不能为空")
    @ApiModelProperty(value = "调整层级(first-章, second-节, third-节内容)")
    private String level;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【章ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【节ID】")
    private Long lessonId;

    @NotEmpty(message = "排序列表不能为空")
    @ApiModelProperty(value = "【排序列表】")
    private List<Long> sortedIds;
}
