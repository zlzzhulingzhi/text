package cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 【课程讲次内容】资源文件名更新【课程讲次内容】参数
 *
 * @author wx
 * @version 1.0
 * @date 2022-09-17 13:50:38
 */
@Data
public class CourseComponentUpdateResourceFileNameRequest {

    @NotNull(message = "resourceFileId不能为空")
    @ApiModelProperty(value = "【资源文件id】")
    private Long resourceFileId;

    @NotBlank(message = "资源文件名不能为空")
    @ApiModelProperty(value = "【资源文件名】")
    private String resourceFileName;

}

