package cn.qbs.wa.teach.exam.admin.pojo.category;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分类树形节点
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:47
 */
@Data
public class CategoryTreeNode extends TreeNode<CategoryTreeNode> {

    @ApiModelProperty("分类名称")
    String name;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

}
