package cn.qbs.wa.teach.course.standard.pojo.coursecategory;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程分类关联关系】(CourseCategory)更新【课程分类关联关系】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CourseCategoryUpdateRequest {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【分类ID】")
    private Long categoryId;

}

