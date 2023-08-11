package cn.qbs.wa.teach.course.standard.pojo.wcourse;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.util.List;

/**
 * 插件-课程表(WCourse)分页查询参数
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
@Data
public class WCoursePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "id数组")
    private List<Long> idList;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程费用类型 1 免费 2 精品")
    private Integer courseFeeType;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(hidden = true)
    private Integer gooded;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【讲师】")
    private String lecturerName;




}

