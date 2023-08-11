package cn.qbs.wa.teach.organization.pojo.newscategory;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 新闻-分类关系表(NewsCategory)更新新闻-分类关系表参数
 *
 * @author makejava
 * @since 2022-01-18 09:30:06
 */
@Data
public class NewsCategoryUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "新闻id")
    private Long newsId;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

}

