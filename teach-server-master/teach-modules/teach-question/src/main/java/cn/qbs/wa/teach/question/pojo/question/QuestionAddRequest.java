package cn.qbs.wa.teach.question.pojo.question;

import cn.qbs.wa.teach.question.pojo.question.option.QuestionOptionTo;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试题(Question)创建试题参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:42:39
 */
@Data
public class QuestionAddRequest {

    @NotNull(message = "题型不能为空！")
    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @NotNull(message = "难度不能为空！")
    @Range(min = 1, max = 5)
    @ApiModelProperty(value = "难度ID")
    private Long difficultyId;

    @DecimalMin(value = "0.5", message = "分数不能小于0.5！")
    @ApiModelProperty(value = "分数")
    private BigDecimal score;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @NotEmpty(message = "分类不能为空！")
    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIds;

    /** 主体部分 */
    @NotBlank(message = "题干不能为空！")
    @ApiModelProperty(value = "题干")
    private String content;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "答案解析")
    private String answerDesc;

    @ApiModelProperty(value = "选项集合")
    private List<QuestionOptionTo> options;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Integer enabled;

}

