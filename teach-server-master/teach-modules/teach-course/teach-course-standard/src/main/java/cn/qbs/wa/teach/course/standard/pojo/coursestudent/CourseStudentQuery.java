package cn.qbs.wa.teach.course.standard.pojo.coursestudent;

import lombok.Data;

import java.util.List;

@Data
public class CourseStudentQuery {
    /**
     * 会员ID
     */
    private List<Long> memberIds;

    /**
     * 课程名称
     */
    private String courseName;
}
