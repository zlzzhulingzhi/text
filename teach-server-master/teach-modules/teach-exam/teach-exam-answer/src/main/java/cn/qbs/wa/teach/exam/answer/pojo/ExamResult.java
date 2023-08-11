package cn.qbs.wa.teach.exam.answer.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-24 9:44
 * @Version 1.0
 */
@Data
public class ExamResult {

    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "证书名称集合")
    private List<String> certNameList;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

    @ApiModelProperty(value = "总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "考试模式 1-标准模式 2-高级模式")
    private Integer examMode;

    @ApiModelProperty(value = "考试开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime examStartTime;

    @ApiModelProperty(value = "考试结束时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime examEndTime;

    @ApiModelProperty(value = "入场开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionStartTime;

    @ApiModelProperty(value = "入场截止时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionEndTime;

    @ApiModelProperty(value = "得分")
    private BigDecimal score;

    @ApiModelProperty(value = "开始答题时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "交卷时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "考试用时(秒数)")
    private Integer useDuration;

    @ApiModelProperty(value = "考试时长(分钟)")
    private Integer duration;

    @ApiModelProperty(value = "考试用时格式化")
    private String useDurationFormat;

    @ApiModelProperty(value = "正确率")
    private BigDecimal correctRate;

    @ApiModelProperty(value = "答对题数")
    private Integer rightCount;

    @ApiModelProperty(value = "答错题数")
    private Integer wrongCount;

    @ApiModelProperty(value = "是否允许查看答题情况")
    private Boolean allowViewingPaperAnswers;

    @ApiModelProperty(value = "显示答案解析")
    private Boolean showAnswerDesc = false;

    @ApiModelProperty(value = "是否发放证书")
    private Boolean showCert = false;

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

        @ApiModelProperty(value = "题型总分")
        private BigDecimal totalScore;

        public BigDecimal getTotalScore() {
            if (!CollectionUtils.isEmpty(this.questionList)) {
                return this.questionList.stream().map(Question::getQuestionScore).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            return null;
        }

    }

    @Data
    public static class Question {

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

        @ApiModelProperty(value = "题干")
        private String content;

        @ApiModelProperty(value = "正确答案")
        private String answer;

        @ApiModelProperty(value = "选项集合")
        private List<QuestionOption> options;

        @ApiModelProperty(value = "答案解析")
        private String answerDesc;

        @ApiModelProperty(value = "考生答案")
        private String examineeAnswer;

        @ApiModelProperty(value = "0 错 1对")
        private Integer correctResult = 0;

        @ApiModelProperty(value = "排序号")
        private Integer sortNum;

    }

}
