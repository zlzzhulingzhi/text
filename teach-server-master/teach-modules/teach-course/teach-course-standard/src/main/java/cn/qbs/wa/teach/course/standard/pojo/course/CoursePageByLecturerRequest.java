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
public class CoursePageByLecturerRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【分类ID】")
    private Long categoryId;

    @ApiModelProperty(value = "【上架状态 0下架 1上架】")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【讲师id】")
    private Long lecturerId;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "课程费用类型 1 免费 2 精品")
    private Integer courseFeeType;

    @ApiModelProperty(value = "id数组")
    private List<Long> idList;

    @ApiModelProperty(value = "学员id")
    private Long studentId;

    @ApiModelProperty(value = "学员id")
    private Long userId;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;

    @ApiModelProperty(value = "【可见用户状态 1 所有人可见 2 部分可见】")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "标签id")
    private List<Long> groupIds;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "【分类ID集合】")
    private List<Long> categoryIdList;
}

