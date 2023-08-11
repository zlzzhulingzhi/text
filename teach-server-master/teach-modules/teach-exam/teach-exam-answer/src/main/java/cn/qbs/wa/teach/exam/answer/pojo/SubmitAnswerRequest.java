package cn.qbs.wa.teach.exam.answer.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 提交答案请求类
 * @Author zcm
 * @Date 2021-12-22 9:22
 * @Version 1.0
 */
@Data
public class SubmitAnswerRequest {

    @NotEmpty(message = "考生试题ID不能为空")
    @ApiModelProperty(value = "考生试题ID")
    private Long examineeQuestionId;

    @NotEmpty(message = "考生答案不能为空")
    @ApiModelProperty(value = "考生答案")
    private String answer;

}
