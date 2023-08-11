package cn.qbs.wa.teach.exam.admin.pojo.category;

import cn.qbs.wa.teach.exam.common.entity.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分类(Category)分类详情
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:52
 */
@Data
public class CategoryDetailResponse extends Category {

    @ApiModelProperty(value = "子分类")
    private List<Category> children;

    @ApiModelProperty(value = "父分类")
    private Category parent;

}

