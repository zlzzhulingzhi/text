package cn.qbs.wa.teach.exam.answer.pojo.center;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 考试表(Exam)分页查询考试表响应
 *
 * @author zcm
 * @since 2021-12-14 13:52:10
 */
@Data
public class PageResponse extends Exam {

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "最低分")
    private BigDecimal lowestScore;

    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ApiModelProperty(value = "学员当前考试状态 1-未开始 2-进行中 3-已完成")
    private Integer studentExamStatus;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "剩余考试次数, -1为无数次")
    private Integer remainingTimes;

    @ApiModelProperty(value = "考试记录id")
    private Long examineeRecordId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "考试记录状态,0 未完成 1 待批改 2已批改 3中断")
    private Integer examineeRecordStatus;

    @ApiModelProperty(value = "考试记录状态,0 作废 1启用")
    private Integer examineeRecordEnabled;
}

