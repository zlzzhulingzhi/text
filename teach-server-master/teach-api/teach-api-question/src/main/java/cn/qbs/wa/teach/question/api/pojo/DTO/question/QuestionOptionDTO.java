package cn.qbs.wa.teach.question.api.pojo.DTO.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zcm
 * @Date 2021/11/26 16:58
 * @Version 1.0
 */
@Data
public class QuestionOptionDTO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "试题ID")
    private Long questionId;

    @ApiModelProperty(value = "选项[A-F]")
    private String option;

    @ApiModelProperty(value = "选项内容")
    private String content;

}
