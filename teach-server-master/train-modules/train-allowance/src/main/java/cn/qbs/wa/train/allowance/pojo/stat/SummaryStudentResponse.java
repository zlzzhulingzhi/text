package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SummaryStudentResponse {

    @ApiModelProperty(value = "总学时")
    private Integer totalLessonNum;

    @ApiModelProperty(value = "资助人次")
    private Integer totalStudentNum;

}
