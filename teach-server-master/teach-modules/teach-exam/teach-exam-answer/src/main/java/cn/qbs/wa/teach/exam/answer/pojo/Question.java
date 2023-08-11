package cn.qbs.wa.teach.exam.answer.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author zcm
 * @Date 2021-12-17 14:39
 * @Version 1.0
 */
@Data
public class Question {

    @ApiModelProperty(value = "试题ID")
    private Long id;

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "考生试题ID")
    private Long examineeQuestionId;

    @ApiModelProperty(value = "题目分数")
    private BigDecimal questionScore;

//    @ApiModelProperty(value = "父题标记, 1: 父题 0:小题")
//    private Integer parentFlag;

    /** 主体部分 */
    @ApiModelProperty(value = "题干")
    private String content;

    /**
     * 答案数量，填空题使用此属性
     */
    @ApiModelProperty(value = "答案数量")
    private Integer answerCount;

    @ApiModelProperty(value = "考生答案")
    private String answer;

    @ApiModelProperty(value = "选项集合")
    private List<QuestionOption> options;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

}
