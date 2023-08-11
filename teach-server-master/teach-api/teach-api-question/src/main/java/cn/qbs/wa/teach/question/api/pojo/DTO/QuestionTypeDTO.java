package cn.qbs.wa.teach.question.api.pojo.DTO;/**
 * @Author zcm
 * @Date 2021-12-16 11:54
 * @Version 1.0
 */

import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
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
public class QuestionTypeDTO {

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "题型名称")
    private String questionTypeName;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "试题列表")
    private List<QuestionDetailsDTO> questionList;

    @ApiModelProperty(value = "题型总分")
    private BigDecimal totalScore;

    public BigDecimal getTotalScore() {
        if (!CollectionUtils.isEmpty(this.questionList)) {
            return this.questionList.stream().map(QuestionDetailsDTO::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }

}
