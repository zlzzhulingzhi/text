package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import cn.qbs.wa.teach.course.api.pojo.DTO.chapter.CourseChapterDTO;
import lombok.Data;

import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseContentDTO {

    List<CourseChapterDTO> chapterList;
}
