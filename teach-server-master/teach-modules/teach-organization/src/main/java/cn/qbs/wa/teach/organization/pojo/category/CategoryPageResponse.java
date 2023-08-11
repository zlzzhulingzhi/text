package cn.qbs.wa.teach.organization.pojo.category;

import cn.qbs.wa.teach.organization.entity.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用分类(Category)分页查询通用分类响应
 *
 * @author makejava
 * @since 2022-01-18 09:48:42
 */
@Data
public class CategoryPageResponse extends Category {
    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;
}

