package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ExamRecordQuestionRequest {
    @NotNull(message = "试题ID不能为空！")
    @ApiModelProperty(value = "试题ID")
    private Long id;

    @ApiModelProperty(value = "考生总分")
    private BigDecimal totalScore;

    @NotNull(message = "打分不能为空！")
    @ApiModelProperty(value = "考生该题分数")
    private BigDecimal score;

}
