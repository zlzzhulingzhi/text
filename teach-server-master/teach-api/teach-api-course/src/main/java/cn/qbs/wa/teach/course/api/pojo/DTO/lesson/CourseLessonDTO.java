package cn.qbs.wa.teach.course.api.pojo.DTO.lesson;

import cn.qbs.wa.teach.course.api.pojo.DTO.component.CourseComponentDTO;
import lombok.Data;

import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseLessonDTO {

    private Long id;

    private Long chapterId;

    private Long lessonId;

    private String lessonName;

    private String introduction;

    private Integer sort;

    private List<CourseComponentDTO> componentList;


}
