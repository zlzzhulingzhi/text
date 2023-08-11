package cn.qbs.wa.teach.course.standard.pojo.courselivelink;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
@Data
public class CourseLiveLinkDetailRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

}

