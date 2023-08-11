package cn.qbs.wa.teach.question.api.pojo.DTO.paper;

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
public class CategoryTreeNodeDTO extends TreeNode<CategoryTreeNodeDTO> {

    @ApiModelProperty("分类名称")
    String name;

    @ApiModelProperty(value = "父节点ID串")
    private String parentCode;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "分组ID【1：试题，2：试卷】")
    private Integer groupId;

}
