package cn.qbs.wa.teach.course.standard.pojo.categorydistribution;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程分类分布】(CategoryDistribution)更新【课程分类分布】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Data
public class CategoryDistributionUpdateRequest {

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【分类ID】")
    private Long categoryId;

    @ApiModelProperty(value = "【课程数量】")
    private Integer count;

}

