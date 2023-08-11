package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author makejava
 * @since 2021-11-18 16:46:12
 */
@Data
public class CourseDetailResponseDTO implements Serializable {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【课程简介】")
    private String introduction;

    @ApiModelProperty(value = "【上架状态 0下架 1上架】")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【上架时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "【讲师列表】")
    private List<CourseLecturerDTO> lecturers;
}
