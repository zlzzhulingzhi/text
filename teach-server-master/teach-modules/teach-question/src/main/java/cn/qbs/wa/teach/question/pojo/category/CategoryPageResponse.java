package cn.qbs.wa.teach.question.pojo.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.question.entity.Category;

/**
 * 分类(Category)分页查询分类响应
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:52
 */
@Data
public class CategoryPageResponse extends Category {
    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;
}

