package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yjx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationInfoDTO {

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "评价人数")
    private Integer evaluatorCount;

    @ApiModelProperty(value = "评价分数")
    private BigDecimal score;
}
