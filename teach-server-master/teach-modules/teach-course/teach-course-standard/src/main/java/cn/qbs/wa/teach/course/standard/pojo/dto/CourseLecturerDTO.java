package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author makejava
 * @since 2021-11-18 16:46:12
 */
@Data
public class CourseLecturerDTO {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long id;

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

    @ApiModelProperty(value = "【讲师头衔】")
    private String lecturerTitle;

}
