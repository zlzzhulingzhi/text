package cn.qbs.wa.teach.exam.admin.pojo.exam;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试题分析包裹
 * @Author zcm
 * @Date 2021-12-28 9:44
 * @Version 1.0
 */
@Data
public class QuestionAnalysisWrap {

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "试卷分数/考试总分")
    private BigDecimal paperScore;

    @ApiModelProperty(value = "考试时长,单位是分钟")
    private Integer duration;

    @ApiModelProperty(value = "题型列表")
    private List<QuestionType> questionTypeList;

    @Data
    public static class QuestionType {

        @ApiModelProperty(value = "题型ID")
        private Long questionTypeId;

        @ApiModelProperty(value = "题型名称")
        private String questionTypeName;

        @ApiModelProperty(value = "排序号")
        private Integer sortNum;

        @ApiModelProperty(value = "试题列表")
        private List<Question> questionList;

    }

    @Data
    public static class Question {

        @ApiModelProperty(value = "试题ID")
        private Long id;

        @ApiModelProperty(value = "题型ID")
        private Long questionTypeId;

        @ApiModelProperty(value = "使用次数")
        private Integer useCount;

        @ApiModelProperty(value = "难度ID")
        private Long difficultyId;

        @ApiModelProperty(value = "分数")
        private BigDecimal score;

        @ApiModelProperty(value = "机构ID")
        private Long orgId;

        @ApiModelProperty(value = "父题标记, 1: 父题 0:小题")
        private Integer parentFlag;

        /** 主体部分 */
        @ApiModelProperty(value = "题干")
        private String content;

        @ApiModelProperty(value = "答案")
        private String answer;

        @ApiModelProperty(value = "答案解析")
        private String answerDesc;

        @ApiModelProperty(value = "选项集合")
        private List<Option> options;

        @ApiModelProperty(value = "难度名称")
        private String difficultyName;

        @ApiModelProperty(value = "试题分析")
        private QuestionAnalysis questionAnalysis;

        @Data
        public static class Option {

            @ApiModelProperty(value = "ID")
            private Long id;

            @ApiModelProperty(value = "试题ID")
            private Long questionId;

            @ApiModelProperty(value = "选项[A-F]")
            private String option;

            @ApiModelProperty(value = "选项内容")
            private String content;

        }
    }

}
