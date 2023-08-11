package cn.qbs.wa.teach.course.standard.pojo.course;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 【课程】(Course)复制【课程】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseCopyRequest extends IdRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

}

