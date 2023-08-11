package cn.qbs.wa.teach.course.standard.pojo.course;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【课程】(Course)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CoursePageIndexRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【上架状态 0下架 1上架】")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "课程费用类型 1 免费 2 精品")
    private Integer courseFeeType;

    @ApiModelProperty(value = "id数组")
    private List<Long> idList;

    @ApiModelProperty(value = "分类id数组")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "学员id")
    private Long studentId;

    @ApiModelProperty(value = "标签id")
    private List<Long> groupIds;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "标签id")
    private List<Long> deptIds;
}

