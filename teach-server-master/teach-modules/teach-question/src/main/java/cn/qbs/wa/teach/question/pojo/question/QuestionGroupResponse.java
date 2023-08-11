package cn.qbs.wa.teach.question.pojo.question;

import cn.qbs.wa.teach.question.pojo.question.search.QuestionSearchResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试题分组
 * @Author zcm
 * @Date 2021/11/19 09:35
 * @Version 1.0
 */
@Data
public class QuestionGroupResponse {

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "题型名称")
    private String questionTypeName;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "题型列表")
    private List<QuestionSearchResult> questionList;

    @ApiModelProperty(value = "题型总分")
    private BigDecimal totalScore;

    public BigDecimal getTotalScore() {
        if (!CollectionUtils.isEmpty(this.questionList)) {
            return this.questionList.stream().map(QuestionSearchResult::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }

}
