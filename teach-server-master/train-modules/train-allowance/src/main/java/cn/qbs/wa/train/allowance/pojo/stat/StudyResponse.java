package cn.qbs.wa.train.allowance.pojo.stat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author makejava
 * @since 2022-11-14 15:35:53
 */
@Data
public class StudyResponse {

    @ApiModelProperty(value = "总学时")
    private Integer totalLessonNum;

    @ApiModelProperty(value = "总人数")
    private Integer totalStudentNum;

}
