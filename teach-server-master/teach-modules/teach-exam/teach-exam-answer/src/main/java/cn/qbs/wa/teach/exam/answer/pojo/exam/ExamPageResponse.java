package cn.qbs.wa.teach.exam.answer.pojo.exam;

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
public class ExamPageResponse extends Exam {

    @ApiModelProperty(value = "考生数")
    private Integer examineeCount;

    @ApiModelProperty(value = "批改数")
    private Integer correctCount;

    @ApiModelProperty(value = "提交数")
    private Integer submitCount;

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "最低分")
    private BigDecimal lowestScore;

    @ApiModelProperty(value = "参加考试的人数")
    private Integer count;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal score;

    @ApiModelProperty(value = "是否通过考试 0 未通过 ,1 通过,2 考试进行中")
    private Integer pass;

    @ApiModelProperty(value = "考试记录表的id")
    private Long examineeRecordId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "是否完成 0 进行中 1 已完成  4 未开始")
    private Integer complete;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "答题的状态 0:时间未到 , 1:开始考试, 2:继续考试 , 3:重新考试 , 4:考试结束 , 5:考试次数已用完")
    private Integer paperStatus;

    @ApiModelProperty(value = "剩余考试次数, -1为无数次")
    private Integer remainingTimes;

}

