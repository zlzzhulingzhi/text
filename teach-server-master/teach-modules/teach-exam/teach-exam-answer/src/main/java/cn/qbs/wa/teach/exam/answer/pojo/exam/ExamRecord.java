package cn.qbs.wa.teach.exam.answer.pojo.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamRecord {
    @ApiModelProperty(value = "考试次数 -1为无限考试")
    private Integer limitCount;

    @ApiModelProperty(value = "最高分")
    private BigDecimal topScore;

    @ApiModelProperty(value = "剩余次数 -1为无限次数")
    private Integer remainingTimes;

    @ApiModelProperty(value = "是否完成 0 进行中 1 已完成 2已批改 3作弊 4 未开始")
    private Integer complete;

    @ApiModelProperty(value = "交卷时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal score;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "考试记录表id")
    private Long examineeRecordId;

    @ApiModelProperty(value = "是否纳入计算 0 否 1是")
    private Integer calculated;

    List<ExamRecord> examRecords;
}
