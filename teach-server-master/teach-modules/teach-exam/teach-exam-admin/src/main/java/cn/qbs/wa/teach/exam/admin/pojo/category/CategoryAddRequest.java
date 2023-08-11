package cn.qbs.wa.teach.exam.admin.pojo.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 分类(Category)创建分类参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:08:47
 */
@Data
public class CategoryAddRequest {

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

