package cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseStatisticOverview;
import lombok.EqualsAndHashCode;

/**
 * 【课程统计信息】(CourseStatisticOverview)【课程统计信息】详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseStatisticOverviewDetailResponse extends CourseStatisticOverview {
    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;
}

