package cn.qbs.wa.teach.course.standard.pojo.dto.lite;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLecturerDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class CourseInfoDTO {

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【机构名称】")
    private String orgName;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【课程简介】")
    private String introduction;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【上架时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "加入课程标识 true-已加入 false-未加入")
    private Boolean isSignUp;

    @ApiModelProperty(value = "讲师列表")
    private List<CourseLecturerDTO> lecturers;

}
