package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试中统计
 *
 * @Author zcm
 * @Date 2022-05-20 17:32
 * @Version 1.0
 */
@Data
public class StatisticsInExam {

    @ApiModelProperty(value = "考试ID")
    private Long examId;

    @ApiModelProperty(value = "考生人数")
    private Integer examineeCount;

    @ApiModelProperty(value = "交卷人数")
    private Integer submitCount;

}
