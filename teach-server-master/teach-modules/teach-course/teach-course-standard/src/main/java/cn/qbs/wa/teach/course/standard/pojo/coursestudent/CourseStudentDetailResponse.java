package cn.qbs.wa.teach.course.standard.pojo.coursestudent;

import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【课程学员】(CourseStudent)【课程学员】详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseStudentDetailResponse extends CourseStudent {

    private String lastStudyLesson;

    private String coverUrl;

    private String courseName;

}

