package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 批改请求参数
 * @Author zcm
 * @Date 2022/1/15 17:44
 * @Version 1.0
 */
@Data
public class CorrectRequest {

    @NotNull(message = "考题ID不能为空！")
    @ApiModelProperty(value = "考题ID")
    private Long examineeRecordQuestionId;

    @NotNull(message = "批改分数不能为空！")
    @Min(value = 0, message = "批改分数不能小于0！")
    @ApiModelProperty(value = "批改分数")
    private BigDecimal score;

}
