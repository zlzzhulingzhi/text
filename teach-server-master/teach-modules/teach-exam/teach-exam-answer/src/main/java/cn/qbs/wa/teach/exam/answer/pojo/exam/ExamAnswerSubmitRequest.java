package cn.qbs.wa.teach.exam.answer.pojo.exam;

import cn.qbs.wa.teach.exam.answer.pojo.SubmitAnswerRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yjx
 */
@Data
public class ExamAnswerSubmitRequest {

    @NotNull(message = "考试ID不能为空")
    @ApiModelProperty("考试ID")
    private Long examId;

    @NotNull(message = "考试记录ID不能为空")
    @ApiModelProperty("考试记录ID")
    private Long examineeRecordId;

    @NotEmpty(message = "答题记录不能为空")
    @ApiModelProperty("答题记录")
    private List<SubmitAnswerRequest> submitAnswerList;
}
