package cn.qbs.wa.teach.course.standard.pojo.course;

import lombok.Data;

/**
 * 【课程】(Course)分页查询【课程】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseListResponse {

    private Long id;

    private String courseName;

    private String lecturerNames;

    private String courseType;
}

