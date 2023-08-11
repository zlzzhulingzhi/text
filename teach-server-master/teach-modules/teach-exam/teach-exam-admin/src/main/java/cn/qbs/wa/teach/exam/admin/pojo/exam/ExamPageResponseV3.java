package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.exam.common.entity.Exam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分页查询考试响应
 *
 * @Author zcm
 * @Date 2022-05-20 14:00
 * @Version 1.0
 */
@Data
public class ExamPageResponseV3 extends Exam {

    @ApiModelProperty(value = "试卷名称")
    private String paperName;

    @ApiModelProperty(value = "考生总数")
    private Integer examineeCount;

    @ApiModelProperty(value = "考试状态")
    private Integer status;

    @ApiModelProperty(value = "参考人数")
    private Integer joinCount;

    @ApiModelProperty(value = "已交卷人数")
    private Integer submitCount;

    @ApiModelProperty(value = "已交卷数量")
    private Integer handInCount;

    @ApiModelProperty(value = "考试中人数")
    private Integer underCount;

    @ApiModelProperty(value = "已批阅数")
    private Integer correctCount;

    @ApiModelProperty(value = "最高分")
    private BigDecimal highestScore;

    @ApiModelProperty(value = "最低分")
    private BigDecimal lowestScore;

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "总分")
    private BigDecimal totalScore;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createBy;

    @ApiModelProperty(value = "创建者姓名")
    private String createByName;

    @ApiModelProperty(value = "是否可以监考")
    private Boolean canInvigilate = false;

}

