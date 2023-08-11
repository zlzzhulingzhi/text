package cn.qbs.wa.teach.course.standard.pojo.dto;

import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseUserVisibleDTO extends CourseUserVisible {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号】")
    private String phone;
}
