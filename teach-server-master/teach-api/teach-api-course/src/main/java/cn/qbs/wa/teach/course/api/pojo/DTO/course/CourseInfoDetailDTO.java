package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDTO;
import lombok.Data;

import java.util.List;

/**
 * 【课程信息】(CourseInfo)【课程信息】详情
 *
 * @author yjx
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseInfoDetailDTO {

    private Long orgId;

    private Long courseId;

    private String courseName;

    private Long categoryId;

    private String categoryName;

    private String coverUrl;

    private String introduction;

    private String courseContent;

    private Integer coursePoints;

    private Integer playbackSpeed;

    private Integer playbackDrag;

    private Integer courseMode;

    private Integer userVisibleStatus;

    private Integer discussStatus;

    private List<CourseLecturerDTO> lecturers;

    private CourseContentDTO content;

    private String courseType;

}

