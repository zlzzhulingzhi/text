package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseComponentDTO extends CourseLiveDTO {

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;

    @ApiModelProperty(value = "【组件名称】")
    private String componentName;

    @ApiModelProperty(value = "【组件类型编码 ZL：资料; WD：文档; SP：视频; SJ：试卷; ZB：直播】")
    private String componentTypeCode;

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

    @ApiModelProperty(value = "【播放位置】")
    private Integer playbackPosition;

    @ApiModelProperty(value = "【播放进度】")
    private Integer playbackProgress;

    @ApiModelProperty(value = "【累积学习时长】")
    private Long learningDuration;
}
