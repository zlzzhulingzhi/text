package cn.qbs.wa.teach.exam.answer.pojo.center;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExamineRecordResponse {

    @ApiModelProperty(value = "是否纳入计算 0 否 1是")
    private Boolean calculated;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "学员当前考试状态 0-未完成 2-进行中 3-已完成")
    private Integer studentExamStatus;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "剩余考试次数, -1为无数次")
    private Integer remainingTimes;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
