package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class CourseLessonSimpleResponseDTO {

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【讲次名称】")
    private String lessonName;

    @ApiModelProperty(value = "【讲次简介】")
    private String introduction;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;

    @ApiModelProperty(value = "【组件名称】")
    private String componentName;

    @ApiModelProperty(value = "【上传文件ID】")
    private Long resourceFileId;

    @ApiModelProperty(value = "【文件路径】")
    private String resourceFilePath;

    @ApiModelProperty(value = "【文件类型】")
    private String resourceFileType;

    @ApiModelProperty(value = "【文件时长】")
    private Integer resourceFileDuration;

    @ApiModelProperty(value = "【播放位置】")
    private Integer playbackPosition;

    @ApiModelProperty(value = "【上架状态 0下架 1上架】")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}
