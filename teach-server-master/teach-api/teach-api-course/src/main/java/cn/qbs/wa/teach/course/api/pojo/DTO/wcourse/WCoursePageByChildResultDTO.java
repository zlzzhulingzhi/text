package cn.qbs.wa.teach.course.api.pojo.DTO.wcourse;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 插件-课程表(WCourse)分页查询插件-课程表响应
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
@Data
public class WCoursePageByChildResultDTO extends WCoursePageResultDTO {

    @ApiModelProperty(value = "【组件ID】")
    private Long componentId;


}

