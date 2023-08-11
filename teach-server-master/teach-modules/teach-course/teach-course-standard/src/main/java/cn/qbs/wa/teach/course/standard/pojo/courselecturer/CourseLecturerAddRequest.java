package cn.qbs.wa.teach.course.standard.pojo.courselecturer;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程讲师】(CourseLecturer)创建【课程讲师】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseLecturerAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲师ID】")
    private Long lecturerId;

    @ApiModelProperty(value = "【讲师姓名】")
    private String lecturerName;

    @ApiModelProperty(value = "【讲师头像】")
    private String lecturerHeadImgUrl;

    @ApiModelProperty(value = "【讲师简介】")
    private String lecturerIntro;

}

