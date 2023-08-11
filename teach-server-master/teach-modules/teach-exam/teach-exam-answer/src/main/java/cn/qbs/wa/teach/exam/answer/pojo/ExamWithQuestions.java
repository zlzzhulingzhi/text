package cn.qbs.wa.teach.exam.answer.pojo;/**
 * @Author zcm
 * @Date 2021-12-17 09:32
 * @Version 1.0
 */

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
 * @Date 2021-12-17 09:32
 * @Version 1.0
 */
@Data
public class ExamWithQuestions {

    @ApiModelProperty(value = "考试ID")
    private Long examId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "试卷分数/考试总分")
    private BigDecimal paperScore;

    @ApiModelProperty(value = "考生考试记录ID")
    private Long examineeRecordId;

    @ApiModelProperty(value = "考生ID")
    private Long examineeId;

    @ApiModelProperty(value = "试卷试题总数")
    private Integer QuestionCount;

    @ApiModelProperty(value = "开始答题时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startAnswerTime;

//    @ApiModelProperty(value = "截止（结束）时间")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime endTime;

    @ApiModelProperty(value = "考试时长,单位是分钟")
    private Integer duration;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "考题状态 0: 添加中 1: 已添加")
    private Integer examQuestionStatus;

    private List<QuestionType> questionTypeList;

    @ApiModelProperty(value = "是否支持上传附件")
    private Boolean supportUploadAttachment;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "附件Url")
    private String attachmentUrl;

    public BigDecimal getPaperScore() {
        if (!CollectionUtils.isEmpty(this.questionTypeList)) {
            return this.questionTypeList.stream().flatMap(item -> item.getQuestionList().stream().map(Question::getQuestionScore))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }

}
