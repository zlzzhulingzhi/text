package cn.qbs.wa.teach.exam.admin.pojo.exam;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 试题分析
 * @Author zcm
 * @Date 2021-12-28 9:44
 * @Version 1.0
 */
@Data
public class QuestionAnalysis {

    @ApiModelProperty(value = "试题ID")
    private Long questionId;

    @ApiModelProperty(value = "得分率")
    private Integer scoringRate;

    @ApiModelProperty(value = "平均分")
    private BigDecimal avgScore;

    @ApiModelProperty(value = "答对题数")
    private Integer rightCount;

    @ApiModelProperty(value = "答错题数")
    private Integer wrongCount;

}
