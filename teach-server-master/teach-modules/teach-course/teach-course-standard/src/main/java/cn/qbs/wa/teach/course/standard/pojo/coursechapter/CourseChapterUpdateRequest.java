package cn.qbs.wa.teach.course.standard.pojo.coursechapter;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 【课程章节】(CourseChapter)更新【课程章节】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseChapterUpdateRequest {

    @NotNull(message = "章节ID不能为空")
    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【章节名称】")
    private String chapterName;

    @ApiModelProperty(value = "【章节简介】")
    private String introduction;

    @ApiModelProperty(value = "【讲次数量】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}

