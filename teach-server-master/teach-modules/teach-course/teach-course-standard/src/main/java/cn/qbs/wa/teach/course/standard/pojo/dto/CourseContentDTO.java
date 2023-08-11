package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yjx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseContentDTO extends CourseInfoDTO {

    @ApiModelProperty(value = "【章节列表】")
    List<CourseChapterDTO> chapterList;

}
