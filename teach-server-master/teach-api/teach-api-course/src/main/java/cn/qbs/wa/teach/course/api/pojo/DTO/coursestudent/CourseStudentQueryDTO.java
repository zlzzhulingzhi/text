package cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseStudentQueryDTO {

    /**
     * 会员ID
     */
    private List<Long> memberIds;

    /**
     * 课程名称
     */
    private String courseName;

}
