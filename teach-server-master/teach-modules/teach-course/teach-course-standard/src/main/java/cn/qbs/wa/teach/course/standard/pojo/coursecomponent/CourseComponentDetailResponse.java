package cn.qbs.wa.teach.course.standard.pojo.coursecomponent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import lombok.EqualsAndHashCode;

/**
 * 【课程讲次内容】(CourseComponent)【课程讲次内容】详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseComponentDetailResponse extends CourseComponent {

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;

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
}

