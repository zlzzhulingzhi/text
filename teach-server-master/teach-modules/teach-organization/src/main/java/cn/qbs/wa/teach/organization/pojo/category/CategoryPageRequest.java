package cn.qbs.wa.teach.organization.pojo.category;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import javax.validation.constraints.NotNull;

/**
 * 通用分类(Category)分页查询参数
 *
 * @author makejava
 * @since 2022-01-18 09:48:41
 */
@Data
public class CategoryPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "上级分类id")
    @NotNull(message = "parentId Null")
    private Long parentId;

    @ApiModelProperty(value = "分组类型 1 新闻")
    @NotNull(message = "groupType Null")
    private Integer groupType;

    @ApiModelProperty(value = "分类名称")
    private String name;


    @ApiModelProperty(value = "启用状态 0-未启用 1-已启用")
    private Integer enabled;


}

