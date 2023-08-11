package cn.qbs.wa.teach.course.standard.pojo.wcourse;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 插件-课程表(WCourse)创建插件-课程表参数
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
@Data
public class WCourseAddRequest {

    @ApiModelProperty(value = "主键id组")
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

