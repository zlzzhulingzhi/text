package cn.qbs.wa.teach.question.pojo.question.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 试题过滤统计请求参数
 * @Author zcm
 * @Date 2021/11/17 09:31
 * @Version 1.0
 */
@Data
public class QuestionCountRequest {

    @ApiModelProperty(value = "试题分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "试题分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
