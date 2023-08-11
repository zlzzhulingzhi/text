package cn.qbs.wa.teach.course.standard.pojo.dto.app;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * 【课程】分页查询参数
 *
 * @author yjx
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageCourseDTO extends BasePageRequest {

    @ApiModelProperty(value = "【内外训标识 true-内训，false-外训】")
    private Boolean inner;

    @ApiModelProperty(value = "【分类ID列表】")
    private Set<Long> categoryIds;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;

    @ApiModelProperty(value = "【精品课程 1-免费课程 0-普通课程】")
    private boolean free;

    @ApiModelProperty(value = "加入课程标识 true-已加入 false-未加入")
    private Boolean isSignUp;

    @ApiModelProperty(value = "【课程ID数组】", hidden = true)
    private List<Long> courseIds;

    @ApiModelProperty(value = "【排除的课程ID数组】", hidden = true)
    private List<Long> ignoreCourseIds;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "标签id")
    private List<Long> groupIds;

    @ApiModelProperty(value = "标签id")
    private List<Long> deptIds;
}
