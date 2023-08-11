package cn.qbs.wa.teach.course.standard.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yjx
 */
@Data
public class CourseComponentExtraDTO {

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;

    @ApiModelProperty(value = "【组件名称】")
    private String componentName;

    @ApiModelProperty(value = "【组件类型编码 ZL：资料; WD：文档; SP：视频; SJ：试卷; ZB：直播; EXTRA：其他】")
    private String componentTypeCode;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "开课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "结课日期")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone = "GMT+8")
    private LocalDate endDate;

    @ApiModelProperty(value = "【讲师ID】")
    private Long lecturerId;

    @ApiModelProperty(value = "【讲师姓名】")
    private String lecturerName;

    @ApiModelProperty(value = "【资源名】")
    private String extraName;

    @ApiModelProperty(value = "【资源描述】")
    private String extraDescription;

}
