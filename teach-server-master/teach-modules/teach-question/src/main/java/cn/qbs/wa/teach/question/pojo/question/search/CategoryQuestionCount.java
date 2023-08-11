package cn.qbs.wa.teach.question.pojo.question.search;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类试题数量统计结果
 * @Author zcm
 * @Date 2021/12/6 19:01
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryQuestionCount extends TreeNode<CategoryQuestionCount> {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "数量")
    private Long quantity;

    public CategoryQuestionCount(Long id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

}
