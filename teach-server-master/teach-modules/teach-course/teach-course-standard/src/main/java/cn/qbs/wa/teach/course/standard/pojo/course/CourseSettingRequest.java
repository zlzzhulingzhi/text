package cn.qbs.wa.teach.course.standard.pojo.course;

import cn.qbs.wa.teach.course.common.entity.Course;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseSettingRequest extends Course {

    private static final long serialVersionUID = 4253164002674653977L;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【课时管理】")
    private Integer courseManage;

    @ApiModelProperty(value = "是否精品课程")
    private Integer gooded;
}
