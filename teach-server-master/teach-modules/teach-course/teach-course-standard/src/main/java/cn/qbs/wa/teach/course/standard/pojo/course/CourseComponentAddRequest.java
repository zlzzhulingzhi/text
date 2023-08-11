package cn.qbs.wa.teach.course.standard.pojo.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yjx
 */
@Data
public class CourseComponentAddRequest {

    @NotBlank(message = "讲次内容不能为空")
    @ApiModelProperty(value = "【组件名称】")
    private String componentName;

    @ApiModelProperty(value = "【组件类型编码 ZL：资料; WD：文档; SP：视频; SJ：试卷; ZB：直播; EXTRA：其他】")
    private String componentTypeCode = "EXTRA";

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【上传文件ID】")
    private Long resourceFileId;

    @ApiModelProperty(value = "【文件路径】")
    private String resourceFilePath;

    @ApiModelProperty(value = "【文件名】")
    private String resourceFileName;

    @ApiModelProperty(value = "【文件类型】")
    private String resourceFileType;

    @ApiModelProperty(value = "【文件大小】")
    private Long resourceFileSize;

    @ApiModelProperty(value = "【文件时长】")
    private Integer resourceFileDuration;

    @ApiModelProperty(value = "开课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "结课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    @ApiModelProperty(value = "【讲师ID】")
    private Long lecturerId;

    @ApiModelProperty(value = "【讲师姓名】")
    private String lecturerName;

    @ApiModelProperty(value = "【资源名】")
    private String extraName;

    @ApiModelProperty(value = "【内容描述】")
    private String extraDescription;

}
