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
public class PageCourseVO {

    @ApiModelProperty(value = "【课程ID】")
    private Long id;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【上架时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【机构名称】")
    private String orgName;

    @ApiModelProperty(value = "讲师列表")
    private List<CourseLecturerDTO> lecturers;

}
