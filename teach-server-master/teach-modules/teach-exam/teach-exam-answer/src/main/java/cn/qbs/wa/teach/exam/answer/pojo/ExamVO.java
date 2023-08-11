package cn.qbs.wa.teach.exam.answer.pojo;

import cn.qbs.wa.teach.exam.common.enumerate.ExamStatusEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-21 16:51
 * @Version 1.0
 */
@Data
public class ExamVO {

    @ApiModelProperty(value = "考试ID")
    private Long id;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "考试名称")
    private Long paperId;

    @ApiModelProperty(value = "考试模式 1-标准模式 2-高级模式")
    private Integer examMode;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止（结束）时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "入场开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionStartTime;

    @ApiModelProperty(value = "入场截止时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionEndTime;

    @ApiModelProperty(value = "考试时长,单位是分钟")
    private Integer duration;

    @ApiModelProperty(value = "考试次数 -1为无限考试")
    private Integer limitCount;

    @ApiModelProperty(value = "总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "试卷试题总数")
    private Integer QuestionCount;

    @ApiModelProperty(value = "打乱试题顺序")
    private Boolean shuffleQuestion;

    @ApiModelProperty(value = "显示答案解析")
    private Boolean showAnswerDesc;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @ApiModelProperty(value = "考前须知")
    private String noticeBeforeExam;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "可见用户 1: 所有人可见 2: 部分可见")
    private Integer userVisible;

    @ApiModelProperty(value = "是否已交卷(true/false)")
    private Boolean submitPaper;

    @ApiModelProperty(value = "考生ID")
    private Long examineeId;

    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    @ApiModelProperty(value = "考试状态")
    @Getter(AccessLevel.NONE)
    private ExamStatusEnum examStatus;

    @ApiModelProperty(value = "考试规则")
    private List<ExamRule> examRuleList;

    @ApiModelProperty(value = "剩余次数 -1为无限制")
    private Integer remainingTimes;

    public Integer getExamStatus() {
        if (this.examStatus != null) {
            return this.examStatus.getStatus();
        }

        return null;
    }

    public String getExamStatusDesc() {
        if (this.examStatus != null) {
            return this.examStatus.getName();
        }

        return null;
    }

    @Data
    public static class ExamRule {

        @ApiModelProperty(value = "规则ID")
        private Long id;

        @ApiModelProperty(value = "规则类型 1-防作弊规则 2-试题规则 3-考试规则")
        private Integer type;

        @ApiModelProperty(value = "规则编码")
        private String code;

        @ApiModelProperty(value = "规则名称")
        private String ruleName;

    }

}
