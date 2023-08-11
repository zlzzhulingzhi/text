package cn.qbs.wa.teach.course.standard.pojo.courselivelink;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲义】(CourseLiveLink)创建【课程讲义】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
@Data
public class CourseLiveLinkAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "直播ID不能为空")
    @ApiModelProperty(value = "【直播ID】")
    private Long liveId;

}

