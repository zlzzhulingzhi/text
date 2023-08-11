package cn.qbs.wa.teach.exam.answer.pojo.center;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExamineInfoResponse {

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "考试次数")
    private Integer examTimes;

    @ApiModelProperty(value = "剩余考试次数, -1为无数次")
    private Integer remainingTimes;

    @ApiModelProperty(value = "考试记录")
    private List<ExamineeRecord> records;
}
