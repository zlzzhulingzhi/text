package cn.qbs.wa.teach.exam.answer.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zcm
 * @Date 2021-12-17 14:46
 * @Version 1.0
 */
@Data
public class QuestionOption {

    @ApiModelProperty(value = "选项[A-F]")
    private String option;

    @ApiModelProperty(value = "选项内容")
    private String content;

}
