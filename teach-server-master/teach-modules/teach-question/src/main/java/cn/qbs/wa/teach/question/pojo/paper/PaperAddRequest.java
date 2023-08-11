package cn.qbs.wa.teach.question.pojo.paper;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷(Paper)创建试卷参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:49:03
 */
@Data
public class PaperAddRequest {

    @NotBlank(message = "试卷名称不能为空")
    @Length(max = 50, message = "试卷名称限定在50字以内")
    @ApiModelProperty(value = "试卷名称")
    private String name;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @NotNull(message = "分类不能为空！")
    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIdList;

    @NotEmpty(message = "题型列表不能为空")
    @ApiModelProperty(value = "题型列表")
    private List<QuestionType> questionTypeList;

    @Data
    public static class QuestionType {

        @NotNull(message = "题型ID不能为空")
        @ApiModelProperty(value = "题型ID")
        private Long id;

        @NotEmpty(message = "试题列表不能为空")
        @ApiModelProperty(value = "试题列表")
        private List<Question> questionList;

        @Data
        public static class Question {

            @NotNull(message = "试题ID不能为空")
            @ApiModelProperty(value = "试题ID")
            private Long id;

            @DecimalMin("0.5")
            @ApiModelProperty(value = "试题分数")
            private BigDecimal score;

        }

    }

}

