package cn.qbs.wa.teach.course.standard.pojo.wcourse;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 插件-课程表(WCourse)更新插件-课程表参数
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
@Data
public class WCourseUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "是否精品 0 否 1是")
    private Integer gooded;

}

