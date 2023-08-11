package cn.qbs.wa.teach.question.pojo.question.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 智能挑题请求参数
 * @Author zcm
 * @Date 2021-11-17 9:10
 * @Version 1.0
 */
@Data
public class QuestionRandomSearchParam {

    @ApiModelProperty(value = "试题分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "难度ID列表")
    private List<Integer> difficultyIds;

    @NotEmpty
    @ApiModelProperty(value = "题型和数量")
    private QuestionTypeQuantity questionTypeQuantity;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
