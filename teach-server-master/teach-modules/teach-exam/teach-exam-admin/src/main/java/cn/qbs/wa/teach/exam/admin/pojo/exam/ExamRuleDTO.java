package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试规则
 * @Author zcm
 * @Date 2022-02-15 16:54
 * @Version 1.0
 */
@Data
public class ExamRuleDTO {

    @ApiModelProperty(value = "考试ID")
    private Long examId;

    @ApiModelProperty(value = "规则ID")
    private Long ruleId;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "规则类型 1 防作弊规则 2 试题规则")
    private Integer type;

    @ApiModelProperty(value = "规则编码")
    private String code;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

}
