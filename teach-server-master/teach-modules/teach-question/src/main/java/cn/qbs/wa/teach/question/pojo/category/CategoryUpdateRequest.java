package cn.qbs.wa.teach.question.pojo.category;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类(Category)更新分类参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:52
 */
@Data
public class CategoryUpdateRequest {

    @NotNull(message = "分类ID不能为空")
    @ApiModelProperty(value = "分类ID")
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "父节点ID")
    private Long parentId;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Boolean enabled;

}

