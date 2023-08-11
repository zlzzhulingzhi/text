package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 考试规则配置
 *
 * @Author zcm
 * @Date 2022-05-19 9:58
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRuleConfigDTO {

    @ApiModelProperty(value = "规则类型 1 防作弊规则 2 试题规则")
    private Integer type;

    @ApiModelProperty(value = "规则列表")
    private List<ExamRuleConfigDetailDTO> ruleList;

}
