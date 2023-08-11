package cn.qbs.wa.teach.course.standard.pojo.course;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseChapterDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【课程】(Course)课程章节详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseContentResponse {

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【内容模式 1-章节模式 2-节模式 3-无章节模式】")
    private Integer courseMode;

    @ApiModelProperty(value = "【章节列表】")
    List<CourseChapterDTO> chapterList;
}

