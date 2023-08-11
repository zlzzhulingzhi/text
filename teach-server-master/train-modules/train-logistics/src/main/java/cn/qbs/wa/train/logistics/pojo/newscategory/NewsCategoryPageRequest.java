package cn.qbs.wa.train.logistics.pojo.newscategory;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新闻-分类关系表(NewsCategory)分页查询参数
 *
 * @author makejava
 * @since 2022-01-18 09:30:05
 */
@Data
public class NewsCategoryPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "新闻id")
    private Long newsId;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

}

