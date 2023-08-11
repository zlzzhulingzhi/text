package cn.qbs.wa.teach.question.pojo.question.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 题型和数量
 * @Author zcm
 * @Date 2021/11/17 14:39
 * @Version 1.0
 */
@Data
public class QuestionTypeQuantity {

    @NotNull(message = "题型ID不能为空")
    @ApiModelProperty(value = "题型ID")
    private Long questionId;

    @NotNull(message = "试题数量不能为空")
    @Min(value = 1, message = "试题数量必须大于0")
    @ApiModelProperty(value = "试题数量")
    private Integer quantity;

}
