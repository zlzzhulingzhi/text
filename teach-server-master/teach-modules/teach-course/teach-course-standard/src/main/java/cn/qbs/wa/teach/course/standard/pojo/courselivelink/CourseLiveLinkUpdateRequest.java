package cn.qbs.wa.teach.course.standard.pojo.courselivelink;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 【课程讲义】(CourseLiveLink)更新【课程讲义】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
@Data
public class CourseLiveLinkUpdateRequest {
    @NotNull(message = "ID不能为空！")
    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【直播ID】")
    private Long liveId;

}

