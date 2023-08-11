package cn.qbs.wa.teach.organization.pojo.category;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【课程分类】(Category)分页查询【课程分类】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryTreeResponse extends TreeNode<CategoryTreeResponse> {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "【分类代号】")
    private String categoryCode;

    @ApiModelProperty(value = "【分类名称】")
    private String name;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "包含课程")
    private Integer count;

    @Override
    public int compareTo(CategoryTreeResponse o) {
        if (o.getSort() == null || sort == null) {
            return super.compareTo(o);
        }
        int i = this.sort.compareTo(o.getSort());
        return i == 0 ? this.id.compareTo(o.getId()) : i;
    }
}

