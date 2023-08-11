package cn.qbs.wa.teach.exam.answer.pojo.center;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@Data
public class ExamCommonRequest {

    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    @NotNull(message = "考生ID不能为空")
    @ApiModelProperty(value = "考生ID")
    private Long examineeId;

    @NotNull(message = "考试ID不能为空")
    @ApiModelProperty(value = "考试ID")
    private Long id;
}
