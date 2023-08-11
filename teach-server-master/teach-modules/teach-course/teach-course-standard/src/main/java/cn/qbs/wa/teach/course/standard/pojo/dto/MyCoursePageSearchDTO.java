package cn.qbs.wa.teach.course.standard.pojo.dto;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 【课程】(Course)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyCoursePageSearchDTO extends BasePageRequest {

    @ApiModelProperty(value = "【内外训标识 true-内训，false-外训】")
    private Boolean inner;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【分类ID】")
    private Long categoryId;

    @ApiModelProperty(value = "【分类ID集合】")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "【用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【学习完成状态 0：未完成， 1：已完成】")
    private Integer learningCompletion;
}

