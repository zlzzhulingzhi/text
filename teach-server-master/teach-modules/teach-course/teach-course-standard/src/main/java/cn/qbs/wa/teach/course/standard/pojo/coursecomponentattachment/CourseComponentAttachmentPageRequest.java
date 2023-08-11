package cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程讲次内容附件】(CourseComponentAttachment)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Data
public class CourseComponentAttachmentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

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

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}

