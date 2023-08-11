package cn.qbs.wa.teach.course.api.pojo.DTO.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryAddRequestDTO {

    @ApiModelProperty(value = "【分类名称】")
    private String categoryName;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【上级分类ID】")
    private Long parentId;
}
