package cn.qbs.wa.teach.exam.answer.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-16 11:54
 * @Version 1.0
 */
@Data
public class QuestionType {

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "题型名称")
    private String questionTypeName;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "试题列表")
    private List<Question> questionList;

    @ApiModelProperty(value = "题型总分")
    private BigDecimal totalScore;

    public BigDecimal getTotalScore() {
        if (!CollectionUtils.isEmpty(this.questionList)) {
            return this.questionList.stream().map(Question::getQuestionScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }

}
