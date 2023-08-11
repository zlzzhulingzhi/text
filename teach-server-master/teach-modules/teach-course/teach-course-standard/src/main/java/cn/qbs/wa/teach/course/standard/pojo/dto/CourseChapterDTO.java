package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseChapterDTO {

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【章节ID】")
    private Long chapterId;

    @ApiModelProperty(value = "【章节名称】")
    private String chapterName;

    @ApiModelProperty(value = "【章节简介】")
    private String introduction;

    @ApiModelProperty(value = "【讲次数量】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【讲次列表】")
    private List<CourseLessonDTO> lessonList;
}
