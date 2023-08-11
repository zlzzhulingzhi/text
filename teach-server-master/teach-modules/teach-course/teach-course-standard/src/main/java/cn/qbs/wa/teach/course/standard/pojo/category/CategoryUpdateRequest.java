package cn.qbs.wa.teach.course.standard.pojo.category;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 【课程分类】(Category)更新【课程分类】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Data
public class CategoryUpdateRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【上级分类ID】")
    private Long parentId;

    @ApiModelProperty(value = "【分类代号】")
    private String categoryCode;

    @ApiModelProperty(value = "【分类名称】")
    private String categoryName;

    @ApiModelProperty(value = "【备注】")
    private String remark;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;

    @ApiModelProperty(value = "【【分类图标】")
    private String categoryIconUrl;

}

