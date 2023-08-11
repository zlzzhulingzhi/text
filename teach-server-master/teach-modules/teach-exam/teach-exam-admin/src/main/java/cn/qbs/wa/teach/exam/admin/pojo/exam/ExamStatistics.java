package cn.qbs.wa.teach.exam.admin.pojo.exam;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试统计
 * @Author zcm
 * @Date 2021-12-24 17:13
 * @Version 1.0
 */
@Data
public class ExamStatistics {

    @ApiModelProperty(value = "考试ID")
    private Long id;

    @ApiModelProperty(value = "试卷ID")
    private Long paperId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "考试模式 1-标准模式 2-高级模式")
    private Integer examMode;

    @ApiModelProperty(value = "总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止（结束）时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "入场开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionStartTime;

    @ApiModelProperty(value = "入场截止时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionEndTime;

    @ApiModelProperty(value = "状态 1-未考试 2-考试中 3-考试结束")
    private Integer status;

    @ApiModelProperty(value = "上架状态 0-下架 1-上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "考试时长,单位是分钟")
    private Integer duration;

    @ApiModelProperty(value = "考试次数 -1为无限考试")
    private Integer limitCount;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createBy;

    @ApiModelProperty(value = "创建者姓名")
    private String createByName;

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "最低分")
    private BigDecimal lowestScore;

    @ApiModelProperty(value = "考生数")
    private Integer examineeCount;

    @ApiModelProperty(value = "提交数")
    private Integer submitCount;

    @ApiModelProperty(value = "已批改数")
    private Integer correctCount;

    @ApiModelProperty(value = "是否可以监考")
    private Boolean canInvigilate = false;

//    @ApiModelProperty(value = "未批改数")
//    private Integer unCorrectCount;

}
