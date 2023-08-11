package cn.qbs.wa.teach.course.standard.pojo.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.Category;

/**
 * 【课程分类】(Category)分页查询【课程分类】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Data
public class CategoryPageResponse extends Category {

    @ApiModelProperty(value = "包含课程")
    private Integer count;

    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;

}

