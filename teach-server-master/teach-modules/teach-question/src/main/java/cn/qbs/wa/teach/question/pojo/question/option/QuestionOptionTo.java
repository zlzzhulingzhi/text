package cn.qbs.wa.teach.question.pojo.question.option;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 试题选项(QuestionOption)
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-06 15:19:50
 */
@Data
@NoArgsConstructor
public class QuestionOptionTo {
    
    @ApiModelProperty(value = "ID")
    private Long id;
    
    @ApiModelProperty(value = "试题ID")
    private Integer questionId;

    @NotBlank
    @ApiModelProperty(value = "选项[A-F]")
    private String option;

    @NotBlank
    @ApiModelProperty(value = "选项内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;


    public QuestionOptionTo(@NotBlank String option, @NotBlank String content) {
        this.option = option;
        this.content = content;
    }

}

