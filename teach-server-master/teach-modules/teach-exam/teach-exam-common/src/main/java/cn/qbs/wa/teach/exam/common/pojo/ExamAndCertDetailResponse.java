package cn.qbs.wa.teach.exam.common.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: NQY
 * @Date: 2022/5/16 16:18
 * @Description:
 */
@Data
public class ExamAndCertDetailResponse {
    @ApiModelProperty(value = "考试id")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "机构id")
    private Long studentId;

    @ApiModelProperty(value = "机构id")
    private Long userId;

    @ApiModelProperty(value = "考试名")
    private String examName;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ApiModelProperty(value = "发放证书规则")
    List<TCertRuleDetailResponse> tCertRuleDetailResponseList;
}
