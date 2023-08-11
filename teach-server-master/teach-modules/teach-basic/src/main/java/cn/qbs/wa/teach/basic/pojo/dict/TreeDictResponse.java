package cn.qbs.wa.teach.basic.pojo.dict;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 16:04
 */
@Data
public class TreeDictResponse extends TreeNode<TreeDictResponse> {

    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "字典值")
    private String dictKey;

    @ApiModelProperty(value = "字典名称")
    private String dictValue;

    @ApiModelProperty(value = "启用禁用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
