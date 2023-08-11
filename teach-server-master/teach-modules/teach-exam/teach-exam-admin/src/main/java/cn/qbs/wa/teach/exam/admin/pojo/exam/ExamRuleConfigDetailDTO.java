package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试规则详细配置
 *
 * @Author zcm
 * @Date 2022-05-19 9:58
 * @Version 1.0
 */
@Data
public class ExamRuleConfigDetailDTO {

    @ApiModelProperty(value = "规则ID")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "规则类型 1-防作弊规则 2-试题规则 3-考试规则 4-查看试卷作答情况")
    private Integer type;

    @ApiModelProperty(value = "分组code")
    private String groupCode;

    @ApiModelProperty(value = "规则编码")
    private String code;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "UI类型 [radio, checkbox]")
    private String uiType;

    @ApiModelProperty(value = "是否被选择")
    private Boolean selected;

}
