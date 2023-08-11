package cn.qbs.wa.train.basic.api.pojo.DTO.dict;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 14:34
 */
@Data
public class TreeDictResultDTO extends TreeNode<TreeDictResultDTO> {

    @ApiModelProperty(value = "字典码")
    private String code;

    @ApiModelProperty(value = "字典值")
    private String dictKey;

    @ApiModelProperty(value = "字典名称")
    private String dictValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
