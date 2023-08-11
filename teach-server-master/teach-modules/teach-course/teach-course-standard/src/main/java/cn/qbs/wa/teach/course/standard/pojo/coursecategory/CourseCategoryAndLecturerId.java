package cn.qbs.wa.teach.course.standard.pojo.coursecategory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 *
 * @Author zcm
 * @Date 2022-06-13 16:08
 * @Version 1.0
 */
@Data
public class CourseCategoryAndLecturerId extends IdListRequest {

    @ApiModelProperty(value = "讲师id")
    private Long lecturerId;

    @ApiModelProperty(value = "学员id")
    private Long studentId;

}
