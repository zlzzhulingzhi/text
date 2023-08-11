package cn.qbs.wa.teach.organization.pojo.category;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 通用分类(Category)创建通用分类参数
 *
 * @author makejava
 * @since 2022-01-18 09:48:41
 */
@Data
public class CategoryAddRequest {


    @ApiModelProperty(value = "上级分类id")
    private Long parentId;

    @ApiModelProperty(value = "分组类型 1 新闻")
    private Integer groupType;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "启用状态 0-未启用 1-已启用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;



}

