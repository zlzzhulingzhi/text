package cn.qbs.wa.teach.course.api.pojo.DTO.lecturer;

import lombok.Data;

/**
 * @author yjx
 */
@Data
public class CourseLecturerDTO {

    private Long lecturerId;

    private String lecturerName;

    private String lecturerHeadImgUrl;

    private String lecturerIntro;
}
