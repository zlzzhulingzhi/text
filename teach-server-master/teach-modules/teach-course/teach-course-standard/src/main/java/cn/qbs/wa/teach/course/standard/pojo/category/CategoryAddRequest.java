package cn.qbs.wa.teach.course.standard.pojo.category;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 【课程分类】(Category)创建【课程分类】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Data
public class CategoryAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【上级分类ID】")
    private Long parentId;

    @ApiModelProperty(value = "【分类代号】")
    private String categoryCode;

    @NotBlank(message = "分类名称不能为空")
    @ApiModelProperty(value = "【分类名称】")
    private String categoryName;

    @ApiModelProperty(value = "【备注】")
    private String remark;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;

    @ApiModelProperty(value = "【模板 0-否 1-是】", hidden = true)
    private Integer template;

    @ApiModelProperty(value = "【【分类图标】")
    private String categoryIconUrl;


}

