package cn.qbs.wa.teach.course.standard.pojo.courselivelink;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程讲义】(CourseLiveLink)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
@Data
public class CourseLiveLinkPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【直播ID】")
    private Long liveId;

}

