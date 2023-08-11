package cn.qbs.wa.teach.course.standard.pojo.coursecomponent;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程讲次内容】(CourseComponent)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseComponentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    @ApiModelProperty(value = "【组件名称】")
    private String componentName;

    @ApiModelProperty(value = "【组件类型编码 ZL：资料; WD：文档; SP：视频; SJ：试卷; ZB：直播】")
    private String componentTypeCode;

    @ApiModelProperty(value = "【组织排序】")
    private Integer sort;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}

