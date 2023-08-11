package cn.qbs.wa.teach.course.standard.pojo.categorydistribution;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程分类分布】(CategoryDistribution)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Data
public class CategoryDistributionPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【分类ID】")
    private Long categoryId;

    @ApiModelProperty(value = "【课程数量】")
    private Integer count;

}

